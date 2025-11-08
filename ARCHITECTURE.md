# Architecture Technique - RiseWell

## 📐 Vue d'ensemble

RiseWell suit une architecture **MVVM (Model-View-ViewModel)** avec une couche Repository, optimisée pour Android avec Jetpack Compose.

```
┌─────────────────────────────────────────────────────────┐
│                        UI Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ HomeScreen   │  │  ChatScreen  │  │ProfileScreen │  │
│  │  (Compose)   │  │  (Compose)   │  │  (Compose)   │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
│         │                  │                  │          │
│         └──────────────────┼──────────────────┘          │
│                            ▼                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   ViewModel  │  │ ChatViewModel│  │ProfileViewModel│ │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│                    Domain/Data Layer                     │
│  ┌─────────────────────────────────────────────────┐    │
│  │         RiseWellRepository (Repository)          │    │
│  │  - Coordination logique métier                   │    │
│  │  - Transformation des données                    │    │
│  │  - Gestion prompts & IA                          │    │
│  └─────────────────────────────────────────────────┘    │
│         │                                │                │
│         ▼                                ▼                │
│  ┌─────────────┐                  ┌─────────────┐       │
│  │  Room DB    │                  │ Ollama API  │       │
│  │  - DAOs     │                  │ (Retrofit)  │       │
│  │  - Entities │                  └─────────────┘       │
│  └─────────────┘                                         │
└─────────────────────────────────────────────────────────┘
```

## 🏗️ Couches de l'Application

### 1. UI Layer (Jetpack Compose)

#### Screens
- **HomeScreen** : Sélection des personas
- **ChatScreen** : Interface de conversation
- **ProfileScreen** : Gestion du profil utilisateur

#### Caractéristiques
- 100% Jetpack Compose (pas de XML)
- Material Design 3
- Navigation Compose
- State hoisting
- Recomposition optimisée

### 2. ViewModel Layer

#### Responsabilités
- Gestion de l'état UI (StateFlow)
- Orchestration des appels Repository
- Transformation des données pour la UI
- Gestion du cycle de vie

#### ChatViewModel
```kotlin
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: RiseWellRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    fun sendMessage(text: String)
    fun sendQuickAction(actionType: QuickActionType)
    fun clearError()
}
```

### 3. Repository Layer

#### RiseWellRepository
**Rôle central** : Abstraction entre les ViewModels et les sources de données

**Responsabilités** :
- Accès aux DAOs (Room)
- Appels API (Ollama)
- Construction des prompts
- Gestion du cache
- Transformation des données

```kotlin
@Singleton
class RiseWellRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val userProfileDao: UserProfileDao,
    private val personaSettingDao: PersonaSettingDao,
    private val ollamaApi: OllamaApi
) {
    suspend fun generateAiResponse(
        persona: Persona,
        userMessage: String,
        userProfile: UserProfile?
    ): Result<String>
}
```

### 4. Data Layer

#### Room Database

**Entités** :
- `UserProfile` : Profil de l'utilisateur
- `Conversation` : Conversations avec les personas
- `Message` : Messages individuels
- `PersonaSetting` : Configuration des personas

**DAOs** :
- `UserProfileDao` : CRUD profil
- `ChatDao` : Gestion conversations et messages
- `PersonaSettingDao` : Paramètres personas

#### Network (Retrofit)

**OllamaApi** :
```kotlin
interface OllamaApi {
    @POST("api/generate")
    suspend fun generateResponse(
        @Body request: GenerateRequest
    ): GenerateResponse
}
```

## 🔄 Flux de Données

### Exemple : Envoi d'un Message

```
1. User tape un message dans ChatScreen
   └─> ChatScreen appelle viewModel.sendMessage(text)

2. ChatViewModel traite la demande
   └─> Envoie au Repository
   └─> Met à jour l'état (isLoading = true)

3. RiseWellRepository
   ├─> Sauvegarde le message user (ChatDao)
   ├─> Récupère le profil user (UserProfileDao)
   ├─> Construit le prompt avec le template du persona
   └─> Appelle Ollama API

4. Ollama API traite et répond
   └─> Repository reçoit la réponse

5. Repository sauvegarde la réponse IA
   └─> ChatDao insère le message AI

6. Room émet le nouveau message via Flow
   └─> Repository propage le Flow

7. ViewModel observe le Flow
   └─> Met à jour l'UI state

8. ChatScreen se recompose automatiquement
   └─> Affiche le nouveau message
```

## 🎯 Patterns et Principes

### Single Source of Truth (SSOT)
- Room Database = source de vérité
- Les Flows émettent les changements automatiquement
- Pas de duplication de données

### Separation of Concerns
- UI ne connaît pas Room ni Retrofit
- ViewModels ne connaissent que le Repository
- Repository orchestre les sources de données

### Dependency Injection (Hilt)
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase
    
    @Provides @Singleton
    fun provideRepository(/* deps */): RiseWellRepository
}
```

### Unidirectional Data Flow
```
User Action → ViewModel → Repository → Data Sources
     ↑                                        │
     └────────── State Update ←───────────────┘
```

## 🧩 Modules et Dépendances

### Core Dependencies

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose")

// Architecture Components
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
implementation("androidx.lifecycle:lifecycle-runtime-compose")

// Room
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")
kapt("androidx.room:room-compiler")

// Retrofit
implementation("com.squareup.retrofit2:retrofit")
implementation("com.squareup.retrofit2:converter-gson")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")

// Hilt
implementation("com.google.dagger:hilt-android")
kapt("com.google.dagger:hilt-compiler")
```

## 🔐 Gestion de l'État

### StateFlow Pattern

**Pourquoi StateFlow ?**
- Compatible avec Compose
- Thread-safe
- Gestion automatique du lifecycle
- Valeur initiale requise

```kotlin
// ViewModel
private val _uiState = MutableStateFlow(ChatUiState())
val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

// Update
_uiState.update { it.copy(isLoading = true) }

// Collect in Compose
val uiState by viewModel.uiState.collectAsState()
```

### Room Flow Pattern

```kotlin
// DAO
@Query("SELECT * FROM messages WHERE conversationId = :id")
fun getMessagesByConversation(id: Long): Flow<List<Message>>

// Repository
fun getMessages(id: Long): Flow<List<Message>> {
    return chatDao.getMessagesByConversation(id)
}

// ViewModel
init {
    repository.getMessages(conversationId).collect { messages ->
        _uiState.update { it.copy(messages = messages) }
    }
}
```

## 🚀 Optimisations Implémentées

### 1. Lazy Loading
- LazyColumn pour les messages (pas de limite de mémoire)
- LazyVerticalGrid pour les personas

### 2. Recomposition Minimale
```kotlin
// ❌ Mauvais : toute la Card se recompose
Card {
    Text(viewModel.messages.size.toString())
}

// ✅ Bon : seul le Text se recompose
val count by remember { derivedStateOf { messages.size } }
Card {
    Text(count.toString())
}
```

### 3. Coroutines Scope
- `viewModelScope` : annulation automatique
- `lifecycleScope` : pour les activités

### 4. Database Indexing
```kotlin
@Entity(
    indices = [Index("conversationId")] // Index pour les requêtes
)
data class Message(...)
```

### 5. Network Timeouts
```kotlin
OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()
```

## 🧪 Testabilité

### Architecture testable

```kotlin
// Repository peut être mocké
class ChatViewModelTest {
    @Mock lateinit var repository: RiseWellRepository
    
    @Test
    fun `sendMessage updates state correctly`() {
        // Given
        val viewModel = ChatViewModel(repository, savedStateHandle)
        
        // When
        viewModel.sendMessage("Test")
        
        // Then
        assert(viewModel.uiState.value.isLoading)
    }
}
```

### Injection de test

```kotlin
@HiltAndroidTest
class ChatScreenTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Test
    fun testChatScreen() {
        // Hilt injecte automatiquement les dépendances de test
    }
}
```

## 📊 Schéma de la Base de Données

```sql
┌─────────────────────┐
│   UserProfile       │
├─────────────────────┤
│ PK id               │
│    age              │
│    weightKg         │
│    heightCm         │
│    activityLevel    │
│    goal             │
└─────────────────────┘

┌─────────────────────┐         ┌─────────────────────┐
│   Conversation      │         │   Message           │
├─────────────────────┤         ├─────────────────────┤
│ PK id               │◄────────│ PK id               │
│    persona          │         │ FK conversationId   │
│    createdAt        │         │    sender           │
│    title            │         │    text             │
└─────────────────────┘         │    timestamp        │
                                └─────────────────────┘

┌─────────────────────┐
│  PersonaSetting     │
├─────────────────────┤
│ PK personaName      │
│    promptTemplate   │
│    temperature      │
│    maxTokens        │
│    responseLength   │
│    tone             │
└─────────────────────┘
```

## 🔮 Évolutions Futures

### Phase 2 : Features Avancées
- [ ] Streaming des réponses IA (SSE)
- [ ] Multi-conversation avec navigation
- [ ] Export conversations en Markdown/PDF
- [ ] WorkManager pour notifications

### Phase 3 : Performance
- [ ] Pagination des messages
- [ ] Cache des réponses fréquentes
- [ ] Compression images
- [ ] ProGuard rules optimisées

### Phase 4 : Architecture
- [ ] Migration vers MVI
- [ ] Use Cases (Clean Architecture)
- [ ] Multi-module
- [ ] Kotlin Multiplatform (iOS)

---

**Dernière mise à jour** : Novembre 2025  
**Version** : 1.0.0-MVP
