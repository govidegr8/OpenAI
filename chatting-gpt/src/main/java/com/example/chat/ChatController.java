package com.example.chat;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

	@Autowired
	ChatService chatService;

    @PostMapping("/chat")
    public String chatWithOpenAI(@RequestBody Map<String, String> requestBody) {
    	
    	String userMessage = requestBody.get("userMessage");
        // Make an API request to OpenAI with userMessage
        String openAiResponse = chatService.callOpenAI(userMessage);

        // Extract the response from OpenAI and return it
        String response = chatService.parseOpenAIResponse(openAiResponse);
        return response;
    }       
}
    
