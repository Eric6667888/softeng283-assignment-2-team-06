package nz.ac.auckland.se283.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.auckland.se283.models.ClassifiedUserStory;
import nz.ac.auckland.se283.models.GroundTruthLabel;
import nz.ac.auckland.se283.models.UserStory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Utility class for handling JSON file operations
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Loads user stories from the input JSON file
     */
    public static List<UserStory> loadUserStories(String resourcePath) throws IOException {
        InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return objectMapper.readValue(inputStream, new TypeReference<List<UserStory>>() {});
    }
    
    /**
     * Loads ground truth labels from the ground truth JSON file
     */
    public static List<GroundTruthLabel> loadGroundTruthLabels(String resourcePath) throws IOException {
        InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return objectMapper.readValue(inputStream, new TypeReference<List<GroundTruthLabel>>() {});
    }
    
    /**
     * Writes classified user stories to the output JSON file
     */
    public static void writeClassifiedUserStories(List<ClassifiedUserStory> classifiedStories, String filePath) throws IOException {
        File outputFile = new File(filePath);
        outputFile.getParentFile().mkdirs(); // Ensure directory exists
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, classifiedStories);
    }
    
    /**
     * Parses classified user stories from JSON string (GPT response)
     */
    public static List<ClassifiedUserStory> parseClassifiedUserStories(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, new TypeReference<List<ClassifiedUserStory>>() {});
    }
}
