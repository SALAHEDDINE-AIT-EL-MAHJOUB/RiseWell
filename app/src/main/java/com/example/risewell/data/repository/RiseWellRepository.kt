package com.example.risewell.data.repository

import com.example.risewell.data.local.ChatDao
import com.example.risewell.data.local.PersonaSettingDao
import com.example.risewell.data.local.UserProfileDao
import com.example.risewell.data.model.*
import com.example.risewell.data.network.GenerateRequest
import com.example.risewell.data.network.GeminiApi
import com.example.risewell.data.network.OllamaApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiseWellRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val userProfileDao: UserProfileDao,
    private val personaSettingDao: PersonaSettingDao,
    private val ollamaApi: OllamaApi,
    private val geminiApi: GeminiApi
) {
    // User Profile operations
    fun getUserProfile(): Flow<UserProfile?> = userProfileDao.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfile) {
        if (profile.id == 0L) {
            userProfileDao.insertUserProfile(profile)
        } else {
            userProfileDao.updateUserProfile(profile)
        }
    }

    // Conversation operations
    suspend fun createConversation(persona: Persona): Long {
        val conversation = Conversation(persona = persona)
        return chatDao.insertConversation(conversation)
    }

    fun getConversationById(id: Long): Flow<Conversation?> {
        return chatDao.getConversationById(id)
    }

    fun getAllConversations(): Flow<List<Conversation>> {
        return chatDao.getAllConversations()
    }

    suspend fun deleteConversation(conversation: Conversation) {
        chatDao.deleteConversation(conversation)
    }

    // Message operations
    suspend fun sendUserMessage(conversationId: Long, text: String): Long {
        val message = Message(
            conversationId = conversationId,
            sender = MessageSender.USER,
            text = text
        )
        return chatDao.insertMessage(message)
    }

    suspend fun saveAiMessage(conversationId: Long, text: String): Long {
        val message = Message(
            conversationId = conversationId,
            sender = MessageSender.AI,
            text = text
        )
        return chatDao.insertMessage(message)
    }

    fun getMessagesByConversation(conversationId: Long): Flow<List<Message>> {
        return chatDao.getMessagesByConversation(conversationId)
    }

    // AI interaction
    suspend fun generateAiResponse(
        persona: Persona,
        userMessage: String,
        userProfile: UserProfile?
    ): Result<String> {
        return try {
            val prompt = buildPrompt(persona, userMessage, userProfile)
            val settings = personaSettingDao.getPersonaSettings(persona.name).first()
            
            // Essayer d'abord avec Ollama
            try {
                val request = GenerateRequest(
                    model = "llama3",
                    prompt = prompt,
                    temperature = settings?.temperature ?: 0.7f,
                    max_tokens = settings?.maxTokens ?: 600
                )
                
                val response = ollamaApi.generateResponse(request)
                Result.success(response.response)
            } catch (ollamaError: Exception) {
                // Si Ollama échoue, essayer Gemini comme backup
                if (geminiApi.isConfigured()) {
                    val geminiResponse = geminiApi.generateResponse(prompt)
                    if (geminiResponse != null) {
                        Result.success(geminiResponse)
                    } else {
                        Result.failure(Exception("Ollama et Gemini ont échoué. Vérifiez votre configuration."))
                    }
                } else {
                    Result.failure(Exception("Ollama n'est pas disponible et Gemini n'est pas configuré. Erreur: ${ollamaError.message}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildPrompt(
        persona: Persona,
        userMessage: String,
        userProfile: UserProfile?
    ): String {
        val template = PersonaPrompts.getTemplateForPersona(persona)
        
        val profileData = if (userProfile != null) {
            mapOf(
                "age" to userProfile.age.toString(),
                "weight" to userProfile.weightKg.toString(),
                "height" to userProfile.heightCm.toString(),
                "activity" to userProfile.activityLevel.name.lowercase().replace('_', ' '),
                "goal" to userProfile.goal.name.lowercase().replace('_', ' ')
            )
        } else {
            mapOf(
                "age" to "unknown",
                "weight" to "unknown",
                "height" to "unknown",
                "activity" to "unknown",
                "goal" to "unknown"
            )
        }

        var finalPrompt = template
        profileData.forEach { (key, value) ->
            finalPrompt = finalPrompt.replace("{$key}", value)
        }

        return "$finalPrompt\n\nUser: $userMessage\nAssistant:"
    }

    // Persona settings
    fun getPersonaSettings(personaName: String): Flow<PersonaSetting?> {
        return personaSettingDao.getPersonaSettings(personaName)
    }

    suspend fun savePersonaSetting(setting: PersonaSetting) {
        personaSettingDao.insertPersonaSetting(setting)
    }

    suspend fun initializeDefaultPersonaSettings() {
        val existingSettings = personaSettingDao.getAllPersonaSettings().first()
        if (existingSettings.isEmpty()) {
            Persona.values().forEach { persona ->
                val setting = PersonaSetting(
                    personaName = persona.name,
                    systemPromptTemplate = PersonaPrompts.getTemplateForPersona(persona),
                    temperature = 0.7f,
                    maxTokens = 600,
                    responseLength = ResponseLength.MEDIUM,
                    tone = when (persona) {
                        Persona.COACH -> "encouraging"
                        Persona.NUTRITIONIST -> "professional"
                        Persona.MOTIVATOR -> "upbeat"
                        Persona.CONSULTANT -> "analytical"
                    }
                )
                personaSettingDao.insertPersonaSetting(setting)
            }
        }
    }
}
