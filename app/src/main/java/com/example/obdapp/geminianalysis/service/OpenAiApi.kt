package com.example.obdapp.geminianalysis.service

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface OpenAiApi {

    @POST("v1/chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") auth: String,
        @Body body: ChatRequest
    ): ChatResponse
}

data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>
)

data class Message(
    val role: String = "user",
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)