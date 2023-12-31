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
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    @Value("${openai-api-key}")
    private String chatGPTApiKey;
    @PostMapping
    String categorizeIngredient(@RequestBody String ingredient) {

        // TODO: This should return "vegan", "vegetarian" or "regular" depending on the ingredient.
        ChatGPTResponse response = Objects.requireNonNull(
                WebClient.create()
                        .post()
                        .uri("https://api.openai.com/v1/chat/completions")
                        .header("Authorization", "Bearer " + chatGPTApiKey)
                        .bodyValue(new ChatGPTRequest("gpt-3.5-turbo",
                                List.of(new ChatGPTRequestMessage(
                                        "user",
                                        "In one word is this ingredient either 'vegan', 'vegatarian' or 'regular' if it is neither: "
                                                +ingredient +"?\n"
                                                + "Answer only with one of the three options metioned. If the ingredient is 'vegan' and 'vegetarian' answer with 'vegan'")

                                )))
                        .retrieve()
                        .toEntity(ChatGPTResponse.class)
                        .block()
        ).getBody();
        if(response.choices().size()>0){
            return response.choices().get(0).message().content();
        } else{
            return " ";
        }
    }

}
