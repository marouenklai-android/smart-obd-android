package com.example.obdapp.geminianalysis.service

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LLMApi {
    @POST("v1/chat")
    @Headers("Content-Type: application/json",
        "Authorization: Bearer place_your_api_key_here")
    suspend fun sendMessage(@Body request: LLMRequest): LLMResponse
}

// Request / Response data classes
data class LLMRequest(val message: String)
data class LLMResponse(val response: String)