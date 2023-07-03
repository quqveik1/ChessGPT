package com.kurlic.chessgpt.gpt

data class Message(
    val role: String,
    val content: String
)
data class MessageResponce(val role: String,
                           val content: GPTMove)
