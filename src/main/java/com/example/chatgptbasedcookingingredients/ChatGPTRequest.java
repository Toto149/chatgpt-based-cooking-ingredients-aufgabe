package com.example.chatgptbasedcookingingredients;

import java.util.List;

public record ChatGPTRequest(String model,
                             List<ChatGPTRequestMessage> messages) {
}
