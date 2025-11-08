# RiseWell - Healthy Coach Application 🏋️‍♂️💪

Application mobile Android de coaching santé avec plusieurs personas IA pilotés par Ollama en local.

## 📱 Fonctionnalités

### MVP Implémenté

- ✅ **4 Personas IA distincts** :
  - 🏋️ **Coach Sportif** : Plans d'entraînement personnalisés, conseils fitness
  - 🥗 **Nutritionniste** : Plans de repas, recettes, conseils nutrition
  - 💪 **Motivateur** : Messages motivants, micro-objectifs quotidiens
  - 🩺 **Consultant Bien-être** : Analyse globale, roadmaps santé

- ✅ **Profil Utilisateur**
  - Âge, poids, taille
  - Niveau d'activité (sédentaire, actif, très actif)
  - Objectifs (perte de poids, prise de muscle, santé générale)

- ✅ **Chat Intelligent**
  - Conversations contextuelles avec chaque persona
  - Actions rapides (plan du jour, conseils rapides, plans hebdomadaires)
  - Historique des conversations
  - Messages de bienvenue personnalisés

- ✅ **Persistance des données**
  - Base de données Room locale
  - Sauvegarde profil utilisateur
  - Historique des conversations
  - Paramètres des personas

## 🏗️ Architecture

### Stack Technique

- **Frontend** : Android (Kotlin) + Jetpack Compose
- **Injection de dépendances** : Hilt/Dagger
- **Navigation** : Navigation Compose
- **Base de données** : Room
- **Réseau** : Retrofit + OkHttp
- **Concurrence** : Coroutines + Flow
- **IA** : Ollama (API locale)

### Structure du Projet

```
app/
├── data/
│   ├── local/              # Room database, DAOs, Converters
│   │   ├── AppDatabase.kt
│   │   ├── ChatDao.kt
│   │   ├── UserProfileDao.kt
│   │   ├── PersonaSettingDao.kt
│   │   └── Converters.kt
│   ├── model/              # Entités et modèles de données
│   │   ├── UserProfile.kt
│   │   ├── Conversation.kt
│   │   ├── Message.kt
│   │   └── PersonaSetting.kt
│   ├── network/            # API Ollama
│   │   └── OllamaApi.kt
│   └── repository/         # Couche Repository
│       └── RiseWellRepository.kt
├── di/                     # Dependency Injection
│   └── AppModule.kt
├── ui/
│   ├── screens/
│   │   ├── home/          # Écran de sélection des personas
│   │   ├── chat/          # Écran de conversation
│   │   └── profile/       # Écran de profil utilisateur
│   └── theme/             # Thème Material3
└── MainActivity.kt
```

## 🔧 Configuration

### Prérequis

1. **Android Studio** (dernière version stable)
2. **JDK 11** ou supérieur
3. **Ollama** installé et en cours d'exécution

### Installation d'Ollama

```bash
# Télécharger depuis https://ollama.ai
ollama pull llama3
ollama serve
```

### Configuration du projet

1. Cloner le dépôt :
```bash
git clone <votre-repo>
cd RiseWell
```

2. Ouvrir dans Android Studio

3. **Important** : Configurer l'URL Ollama dans `AppModule.kt` :
   - Pour **émulateur Android** : `http://10.0.2.2:11434/`
   - Pour **appareil physique** : `http://<votre-ip-locale>:11434/`

```kotlin
// di/AppModule.kt
.baseUrl("http://10.0.2.2:11434/") // Émulateur
// ou
.baseUrl("http://192.168.x.x:11434/") // Appareil physique
```

4. Synchroniser Gradle et compiler

## 🚀 Utilisation

### Premiers pas

1. **Créer un profil** : Cliquez sur l'icône profil et renseignez vos informations
2. **Choisir un persona** : Sélectionnez le coach qui vous correspond
3. **Commencer à discuter** : Utilisez les actions rapides ou posez vos questions

### Actions Rapides par Persona

#### 🏋️ Coach Sportif
- Plan du jour
- Plan de la semaine
- Conseil d'exercice rapide

#### 🥗 Nutritionniste
- Menu du jour
- Menu de la semaine
- Astuce nutrition

#### 💪 Motivateur
- Motive-moi!
- Objectif du jour
- Citation inspirante

#### 🩺 Consultant
- Analyse de ma situation
- Roadmap 4 semaines
- Conseil santé global

## 📊 Modèle de Données

### UserProfile
```kotlin
data class UserProfile(
    val id: Long,
    val age: Int,
    val weightKg: Float,
    val heightCm: Int,
    val activityLevel: ActivityLevel,
    val goal: Goal
)
```

### Conversation & Message
```kotlin
data class Conversation(
    val id: Long,
    val persona: Persona,
    val createdAt: LocalDateTime
)

data class Message(
    val id: Long,
    val conversationId: Long,
    val sender: MessageSender, // USER ou AI
    val text: String,
    val timestamp: LocalDateTime
)
```

### PersonaSetting
```kotlin
data class PersonaSetting(
    val personaName: String,
    val systemPromptTemplate: String,
    val temperature: Float,
    val maxTokens: Int,
    val responseLength: ResponseLength,
    val tone: String
)
```

## 🎨 Templates de Prompts

Chaque persona dispose d'un template de prompt optimisé qui intègre automatiquement le profil utilisateur :

```kotlin
// Exemple : Coach Sportif
"""You are a friendly fitness coach. 
User profile: age={age}, weight={weight}kg, height={height}cm, 
activity={activity}, goal={goal}.
Generate personalized workout advice, exercise plans, and form tips. 
Keep it safe and appropriate for the user's level.
Tone: encouraging and motivational."""
```

Les variables `{age}`, `{weight}`, etc. sont automatiquement remplacées par les données du profil utilisateur.

## 🔒 Sécurité & Vie Privée

- ✅ **Toutes les données restent locales** (Room database)
- ✅ **IA locale avec Ollama** (pas de données envoyées au cloud)
- ✅ **Pas de tracking ni d'analytics tiers**
- ✅ **Chiffrement de la base de données** (à activer en production)

## 🚧 Améliorations Futures

### Fonctionnalités
- [ ] Notifications quotidiennes (WorkManager)
- [ ] Historique des conversations (écran dédié)
- [ ] Export des plans en PDF
- [ ] Graphiques de progression
- [ ] Mode sombre
- [ ] Multi-utilisateurs

### Technique
- [ ] Tests unitaires (JUnit, MockK)
- [ ] Tests UI (Compose Testing)
- [ ] CI/CD (GitHub Actions)
- [ ] Chiffrement Room
- [ ] Backup automatique
- [ ] Support multi-langues

## 📝 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👥 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :
1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📧 Contact

Pour toute question ou suggestion : [votre-email]

---

**Fait avec ❤️ et Kotlin + Jetpack Compose**
