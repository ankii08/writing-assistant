package com.ankit.project.writing_assistant.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    // GPT-4 Turbo Text Generation
    public String generateText(String prompt) throws Exception {
        System.out.println("Generating text for prompt: " + prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4-turbo"); // Updated to GPT-4 Turbo

        // Prepare the messages array with role and content
        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of("role", "user", "content", prompt)
        );

        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 4000);

        Gson gson = new Gson();
        String body = gson.toJson(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response from OpenAI: " + response.body());

        Map<String, Object> responseBody = gson.fromJson(response.body(), Map.class);
        Map<String, Object> firstChoice = ((List<Map<String, Object>>) responseBody.get("choices")).get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        return message.get("content").toString();
    }

    // DALL·E Image Generation
    public String generateImage(String prompt) throws Exception {
        System.out.println("Generating image for prompt: " + prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        requestBody.put("n", 1);
        requestBody.put("size", "1024x1024");

        Gson gson = new Gson();
        String body = gson.toJson(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.openai.com/v1/images/generations"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response from OpenAI (Image): " + response.body());

        Map<String, Object> responseBody = gson.fromJson(response.body(), Map.class);
        return ((List<Map<String, Object>>) responseBody.get("data")).get(0).get("url").toString();
    }
}
