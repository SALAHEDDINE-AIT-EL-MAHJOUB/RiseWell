package com.example.risewell.data.network

import retrofit2.http.Body
import retrofit2.http.POST

interface OllamaApi {
    @POST("api/generate")
    suspend fun generateResponse(@Body request: GenerateRequest): GenerateResponse
}

data class GenerateRequest(
    val model: String = "llama2",
    val prompt: String,
    val temperature: Float = 0.7f,
    val max_tokens: Int = 600
)

data class GenerateResponse(
    val model: String,
    val response: String,
    val done: Boolean
)
