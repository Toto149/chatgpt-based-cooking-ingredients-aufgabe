package com.example.chatgptbasedcookingingredients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/cooking")
@RequiredArgsConstructor
public class CookingGPTController {
    @Value("${OPEN_AI_API_KEY}")
    private String chatGPTApiKey;
    @PostMapping
    String cookingInstructions(@RequestBody String ingredients){

        ChatGPTResponse response = Objects.requireNonNull(
                WebClient.create()
                        .post()
                        .uri("https://api.openai.com/v1/chat/completions")
                        .header("Authorization", "Bearer " + chatGPTApiKey)
                        .bodyValue(new ChatGPTRequest(
                                "gpt-3.5-turbo",
                                List.of(new ChatGPTRequestMessage(
                                        "user",
                                        "Give me a short and consice cooking instructions for a dish " +
                                                "with these following ingredients:\n"
                                        + ingredients + "."

                                ))
                        )
                        )
                        .retrieve()
                        .toEntity(ChatGPTResponse.class)
                        .block()

        ).getBody();

        if(response.choices().size()>0){
            return  response.choices().get(0).message().content();
        }
        else{
            return " ";
        }
    }
}
