package com.example.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatService {
	
	@Value("${openai.api.key}")
	private String openAiApiKey;
	
	@Value("${openai.api.url}")
    private String openAiEndpoint;
	
	
	public String callOpenAI(String userMessage) {
		// Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAiApiKey);

        // Set up the request body (user message)
        String requestBody = "{\"prompt\": \"" + userMessage + "\"}";

        // Create the HTTP entity containing headers and body
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Make the API request using RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(openAiEndpoint, HttpMethod.POST, entity, String.class);

        // Get the response body from the ResponseEntity
        return response.getBody();
    }

    public String parseOpenAIResponse(String openAiResponse) {
    	try {
            // Parse the JSON response using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseNode = objectMapper.readTree(openAiResponse);

            // Extract the generated message from the 'choices' array
            JsonNode choicesNode = responseNode.get("choices");
            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode choice = choicesNode.get(0);
                JsonNode textNode = choice.get("text");
                if (textNode != null && textNode.isTextual()) {
                    return textNode.asText();
                }
            }

            // If the response structure is not as expected, return an error message or throw an exception
            return "Error: Unable to parse OpenAI response.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Failed to parse OpenAI response.";
        }
    }

}
