package com.example.chatgptbasedcookingingredients;

import java.util.List;

public record ChatGPTResponse(List<ChatGPTResponseChoice> choices) {
}
