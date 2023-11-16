package com.kurlic.chessgpt.gpt

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GPTApi {
    @POST("https://api.openai.com/v1/chat/completions")
    fun chatWithGPT3(@Body requestBody: ChatRequest): Call<ChatResponse>
}