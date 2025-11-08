package com.example.risewell.ui.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.risewell.data.model.*
import com.example.risewell.data.repository.RiseWellRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: RiseWellRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val personaArg = savedStateHandle.get<String>("persona")
    private val persona = Persona.valueOf(personaArg ?: Persona.COACH.name)

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var currentConversationId: Long? = null

    init {
        viewModelScope.launch {
            createNewConversation()
            observeMessages()
            observeUserProfile()
        }
    }

    private suspend fun createNewConversation() {
        currentConversationId = repository.createConversation(persona)
    }

    private fun observeMessages() {
        viewModelScope.launch {
            currentConversationId?.let { id ->
                repository.getMessagesByConversation(id).collect { messages ->
                    _uiState.update { it.copy(messages = messages) }
                }
            }
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            repository.getUserProfile().collect { profile ->
                _uiState.update { it.copy(userProfile = profile) }
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            val conversationId = currentConversationId ?: return@launch
            
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Save user message
                repository.sendUserMessage(conversationId, text)

                // Generate AI response
                val profile = uiState.value.userProfile
                val result = repository.generateAiResponse(persona, text, profile)

                result.fold(
                    onSuccess = { aiResponse ->
                        repository.saveAiMessage(conversationId, aiResponse)
                        _uiState.update { it.copy(isLoading = false) }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Erreur: ${error.message}"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erreur inattendue: ${e.message}"
                    )
                }
            }
        }
    }

    fun sendQuickAction(actionType: QuickActionType) {
        val actionMessage = when (actionType) {
            QuickActionType.DAILY_PLAN -> when (persona) {
                Persona.COACH -> "Donne-moi un plan d'entraînement pour aujourd'hui"
                Persona.NUTRITIONIST -> "Propose-moi un plan de repas pour aujourd'hui"
                Persona.MOTIVATOR -> "Donne-moi une motivation pour aujourd'hui"
                Persona.CONSULTANT -> "Quelle est mon analyse de progression aujourd'hui?"
            }
            QuickActionType.QUICK_TIP -> "Donne-moi un conseil rapide"
            QuickActionType.WEEKLY_PLAN -> "Crée-moi un plan pour la semaine"
            QuickActionType.MOTIVATION -> "Motive-moi!"
        }
        sendMessage(actionMessage)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
