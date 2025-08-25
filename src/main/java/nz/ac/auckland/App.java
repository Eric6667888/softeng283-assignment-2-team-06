package nz.ac.auckland;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.se283.GptClient;
import nz.ac.auckland.se283.prompts.PromptEngineering;


public class App {

    public static void main(String[] args) throws Exception {
        
        GptClient client = new GptClient();

        String input_content = "Your input content here";

        String systemPrompt = PromptEngineering.getPrompt("prompt");

        System.out.println(systemPrompt);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", systemPrompt));
        messages.add(
            new ChatMessage("user", input_content));

        ChatCompletionResult result = client.runOnce(messages, 1, 0.2, 1.0, 2000);
        String response = result.getFirstChoice().getChatMessage().getContent();
        System.out.println(response);
  
    }
}
