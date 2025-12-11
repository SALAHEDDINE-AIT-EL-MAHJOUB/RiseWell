

Application mobile Android de coaching santé avec plusieurs personas IA pilotés  et Gemini AI (backup cloud).

## 👥 Membres du groupe

- [salaheddine ait el mahjoub]
- [omar essafi ]
- [yahya gabbas]

## 📱 Fonctionnalités

### MVP Implémenté

- ✅ **4 Personas IA distincts** :
  - 🏋️ **Coach Sportif** : Plans d'entraînement personnalisés, conseils fitness
  - 🥗 **Nutritionniste** : Plans de repas, recettes, conseils nutrition
  - 💪 **Motivateur** : Messages motivants, micro-objectifs quotidiens
  - 🩺 **Consultant Bien-être** : Analyse globale, roadmaps santé

- ✅ **Dual AI Backend (Ollama + Gemini)**
  - Ollama local (prioritaire) pour confidentialité
  - Gemini AI cloud comme backup automatique
  - Bascule transparente si Ollama indisponible

- ✅ **Profil Utilisateur**
  - Âge, poids, taille
  - Niveau d'activité (sédentaire, actif, très actif)
  - Objectifs (perte de poids, prise de muscle, santé générale)

- ✅ **Chat Intelligent**
  - Conversations contextuelles avec chaque persona
  - Actions rapides (plan du jour, conseils rapides, plans hebdomadaires)
  - Historique des conversations
  - Messages de bienvenue personnalisés
  - Indicateurs de chargement et gestion d'erreurs

- ✅ **Persistance des données**
  - Base de données Room locale
  - Sauvegarde profil utilisateur
  - Historique des conversations
  - Paramètres des personas

## 🏗️ Architecture

### Stack Technique

- **Frontend** : Android (Kotlin 2.0.21) + Jetpack Compose + Material3
- **Injection de dépendances** : Hilt/Dagger 2.48.1 (avec KSP)
- **Navigation** : Navigation Compose
- **Base de données** : Room 2.6.0
- **Réseau** : Retrofit 2.9.0 + OkHttp 4.11.0
- **Concurrence** : Coroutines + Flow
- **IA** : 
  - Ollama (API locale - llama3)
  - Google Gemini AI 0.1.2 (backup cloud)
- **Build** : Gradle 8.13.0 + Java 20

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
│   ├── network/            # APIs
│   │   ├── OllamaApi.kt   # API Ollama (local)
│   │   └── GeminiApi.kt   # API Gemini (backup)
│   └── repository/         # Couche Repository
│       └── RiseWellRepository.kt
├── di/                     # Dependency Injection
│   └── AppModule.kt
├── ui/
│   ├── screens/
│   │   ├── home/          # Écran de sélection des personas
│   │   ├── chat/          # Écran de conversation
│   │   │   ├── ChatScreen.kt
│   │   │   ├── ChatViewModel.kt
│   │   │   └── ChatUtils.kt
│   │   └── profile/       # Écran de profil utilisateur
│   └── theme/             # Thème Material3
└── MainActivity.kt
```

## 🔧 Configuration

### Prérequis

1. **Android Studio** (dernière version stable)
2. **JDK 17+** (ou JDK 20)
3. **Ollama** installé et en cours d'exécution (optionnel grâce au backup Gemini)
4. **Clé API Gemini** (gratuite sur [Google AI Studio](https://makersuite.google.com/app/apikey))

### Installation d'Ollama (Optionnel)

```bash
# Télécharger depuis https://ollama.ai
ollama pull llama3
ollama serve
```

### Configuration du projet

1. Cloner le dépôt :
```bash
git clone https://github.com/UniCA-EMSI-CASA-IA2/td3-2025-salah_yahya.git
cd td3-2025-salah_yahya/RiseWell
```

2. Configurer la clé API Gemini dans `gradle.properties` :
```properties
GEMINI_API_KEY=VOTRE_CLE_API_ICI
```

3. **Important** : Configurer l'URL Ollama dans `AppModule.kt` (si vous utilisez Ollama) :
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

### Système AI Dual (Ollama + Gemini)

L'application fonctionne avec un système de fallback automatique :

1. **Priorité à Ollama** (si disponible) :
   - ✅ Confidentialité totale (tout reste local)
   - ✅ Gratuit et illimité
   - ✅ Rapide

2. **Fallback vers Gemini** (si Ollama indisponible) :
   - ✅ Aucune configuration Ollama nécessaire
   - ✅ Fonctionne partout (cloud)
   - ✅ Bascule transparente

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

## 🔒 Sécurité & Vie Privée

- ✅ **Données locales par défaut** (Room database + Ollama local)
- ✅ **Backup cloud sécurisé** (Gemini AI avec HTTPS)
- ✅ **Pas de tracking ni d'analytics tiers**
- ✅ **Chiffrement des communications** (TLS)
- ✅ **Clé API utilisateur** (chacun utilise sa propre clé)

## 🎯 Objectifs Atteints

- ✅ Intégration de 2 modèles d'IA (Ollama local + Gemini cloud)
- ✅ Architecture MVVM complète avec Repository Pattern
- ✅ UI Material3 moderne avec Jetpack Compose
- ✅ Persistance des données avec Room
- ✅ Injection de dépendances avec Hilt
- ✅ Gestion d'erreurs et fallback automatique
- ✅ 4 personas distincts avec prompts personnalisés
- ✅ System de profil utilisateur complet
- ✅ Build Gradle optimisé (KSP au lieu de Kapt)

## 🚧 Améliorations Futures

### Fonctionnalités
- [ ] Notifications quotidiennes (WorkManager)
- [ ] Export des plans en PDF
- [ ] Graphiques de progression
- [ ] Mode sombre
- [ ] Multi-utilisateurs

### Technique
- [ ] Tests unitaires (JUnit, MockK)
- [ ] Tests UI (Compose Testing)
- [ ] CI/CD (GitHub Actions)
- [ ] Chiffrement Room
- [ ] Support multi-langues

### Liens vers la vidéo

[Plan de la vidéo et Script Audio](VIDEO_PLAN.md)

### Démonstration

https://github.com/user-attachments/assets/placeholder-guid

> **Note** : Si la vidéo ne s'affiche pas directement ci-dessous, vous pouvez la télécharger ou la voir ici : [Vidéo de démonstration](app/src/main/res/drawable/Vidéo%20de%20démonstration.mp4)

<video src="app/src/main/res/drawable/Vidéo%20de%20démonstration.mp4" controls="controls" style="max-width: 100%;">
  Votre navigateur ne supporte pas la lecture de vidéos.
</video>



## 👥 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :
1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

---

