package nz.ac.auckland;

import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.se283.GptClient;
import nz.ac.auckland.se283.models.ClassifiedUserStory;
import nz.ac.auckland.se283.models.GroundTruthLabel;
import nz.ac.auckland.se283.models.UserStory;
import nz.ac.auckland.se283.prompts.PromptEngineering;
import nz.ac.auckland.se283.utils.JsonUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        
        // Load data
        System.out.println("Loading input data...");
        List<UserStory> userStories = JsonUtils.loadUserStories("input/input.json");
        List<GroundTruthLabel> groundTruthLabels = JsonUtils.loadGroundTruthLabels("ground_truth/ground_truth_labels.json");
        
        System.out.println("Loaded " + userStories.size() + " user stories");
        System.out.println("Loaded " + groundTruthLabels.size() + " ground truth labels");
        
        // Create ground truth lookup map for easier access
        Map<String, Integer> groundTruthMap = new HashMap<>();
        for (GroundTruthLabel label : groundTruthLabels) {
            groundTruthMap.put(label.getUs_id().toLowerCase(), label.getGround_truth_problem_id());
        }
        
        // Initialize GPT client
        GptClient client = new GptClient();
        String systemPrompt = PromptEngineering.getPrompt("prompt");
        
        // Convert user stories to JSON string for GPT
        StringBuilder userStoriesJson = new StringBuilder();
        userStoriesJson.append("[");
        for (int i = 0; i < userStories.size(); i++) {
            UserStory story = userStories.get(i);
            if (i > 0) userStoriesJson.append(",");
            userStoriesJson.append("{\"us_id\":\"").append(story.getUS_ID())
                          .append("\",\"text\":\"").append(story.getUS_text().replace("\"", "\\\""))
                          .append("\"}");
        }
        userStoriesJson.append("]");
        String userStoriesJsonString = userStoriesJson.toString();
        
        // Prepare messages for GPT
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", systemPrompt));
        messages.add(new ChatMessage("user", userStoriesJsonString));
        
        // Call GPT to classify user stories
        System.out.println("Processing user stories with GPT...");
        ChatCompletionResult result = client.runOnce(messages, 1, 0.1, 1.0, 4000);
        String gptResponse = result.getFirstChoice().getChatMessage().getContent();
        
        // Parse GPT response
        List<ClassifiedUserStory> classifiedStories;
        try {
            // Clean up smart quotes and other Unicode characters that can break JSON parsing
            String cleanedResponse = gptResponse
                .replace("\u201C", "\"")  // Left double quotation mark
                .replace("\u201D", "\"")  // Right double quotation mark
                .replace("\u2018", "'")   // Left single quotation mark
                .replace("\u2019", "'")   // Right single quotation mark
                .replace("\u2013", "-")   // En dash
                .replace("\u2014", "-")   // Em dash
                .replace("?s", "'s")      // Common smart quote issue with possessives
                .replace("?t", "'t")      // Common smart quote issue with contractions
                .replace("?", "'");       // General fallback for remaining smart quotes
            
            classifiedStories = JsonUtils.parseClassifiedUserStories(cleanedResponse);
        } catch (Exception e) {
            System.err.println("Error parsing GPT response. Raw response:");
            System.err.println(gptResponse);
            throw new RuntimeException("Failed to parse GPT response", e);
        }
        
        // Write output to file
        String outputPath = "target/output/output.json";
        JsonUtils.writeClassifiedUserStories(classifiedStories, outputPath);
        System.out.println("Output written to: " + outputPath);
        
        // Calculate accuracy
        int correctPredictions = 0;
        int totalPredictions = 0;
        
        for (ClassifiedUserStory classified : classifiedStories) {
            String usId = classified.getUs_id().toLowerCase();
            // Try different ID formats: "1" -> "us1", "US1" -> "us1", etc.
            String normalizedId = usId.startsWith("us") ? usId : "us" + usId.replaceAll("[^0-9]", "");
            Integer groundTruth = groundTruthMap.get(normalizedId);
            
            if (groundTruth != null) {
                totalPredictions++;
                if (classified.getEstimated_problem_id() == groundTruth) {
                    correctPredictions++;
                }
                System.out.println("Story " + classified.getUs_id() + 
                    ": Predicted=" + classified.getEstimated_problem_id() + 
                    ", Actual=" + groundTruth + 
                    ", " + (classified.getEstimated_problem_id() == groundTruth ? "CORRECT" : "INCORRECT"));
            } else {
                System.err.println("Warning: No ground truth found for story: " + classified.getUs_id() + " (normalized: " + normalizedId + ")");
            }
        }
        
        // Calculate and display accuracy
        double accuracy = totalPredictions > 0 ? (double) correctPredictions / totalPredictions * 100 : 0.0;
        DecimalFormat df = new DecimalFormat("#.##");
        
        System.out.println("\n=== RESULTS ===");
        System.out.println("Total predictions: " + totalPredictions);
        System.out.println("Correct predictions: " + correctPredictions);
        System.out.println("Accuracy: " + df.format(accuracy) + "%");
    }
}
