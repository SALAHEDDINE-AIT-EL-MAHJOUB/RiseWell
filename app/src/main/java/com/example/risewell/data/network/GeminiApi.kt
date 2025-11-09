package com.example.risewell.data.network

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service pour interagir avec l'API Gemini de Google
 * Utilisé comme backup si Ollama n'est pas disponible
 */
@Singleton
class GeminiApi @Inject constructor(
    private val apiKey: String
) {
    private val model by lazy {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
                topK = 40
                topP = 0.95f
                maxOutputTokens = 600
            }
        )
    }

    /**
     * Génère une réponse à partir d'un prompt
     * @param prompt Le prompt à envoyer à Gemini
     * @return La réponse générée ou null en cas d'erreur
     */
    suspend fun generateResponse(prompt: String): String? {
        return try {
            val response = model.generateContent(prompt)
            response.text
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Vérifie si la clé API est configurée
     */
    fun isConfigured(): Boolean {
        // <-- MODIFICATION ICI -->
        // Cette logique est correcte.
        // Elle renvoie 'true' si la clé N'EST PAS la fausse clé d'exemple.
        return apiKey.isNotEmpty() && apiKey != "AIzaSyCWqNvRAWJQON-rb3SmmzQ_HIzBtNBN8Mc"
    }
}