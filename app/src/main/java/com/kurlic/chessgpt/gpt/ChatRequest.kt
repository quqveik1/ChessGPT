package com.kurlic.chessgpt.gpt

data class ChatRequest(
    val messages: List<Message>,
    val model: String = "gpt-3.5-turbo",
)
