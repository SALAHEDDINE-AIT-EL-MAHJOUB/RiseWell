# 🔧 Troubleshooting & FAQ - RiseWell

## 🚨 Problèmes Courants et Solutions

### 1. Erreurs de Connexion Ollama

#### Symptôme
```
Error: Failed to connect to Ollama
Connection refused
```

#### Diagnostic
```bash
# Tester si Ollama répond
curl http://localhost:11434/api/tags

# Vérifier le processus
# Windows
tasklist | findstr ollama

# macOS/Linux
ps aux | grep ollama
```

#### Solutions

**A. Ollama non démarré**
```bash
ollama serve
```

**B. Port incorrect**
Vérifier dans `AppModule.kt` :
```kotlin
.baseUrl("http://10.0.2.2:11434/") // Émulateur
```

**C. Pare-feu bloque**
```bash
# Windows : Autoriser ollama dans le pare-feu
netsh advfirewall firewall add rule name="Ollama" dir=in action=allow protocol=TCP localport=11434

# macOS
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --add /usr/local/bin/ollama
```

**D. Cleartext traffic (Android)**
Vérifier dans `AndroidManifest.xml` :
```xml
android:usesCleartextTraffic="true"
```

---

### 2. Modèle Non Trouvé

#### Symptôme
```
Error: model 'llama3' not found
```

#### Solution
```bash
# Lister les modèles installés
ollama list

# Si llama3 n'est pas là
ollama pull llama3

# Vérifier à nouveau
ollama list
```

#### Modèles alternatifs (plus légers)
```bash
# Plus rapide mais moins précis
ollama pull llama3:8b

# Très léger pour les tests
ollama pull phi
```

Puis modifier `OllamaApi.kt` :
```kotlin
data class GenerateRequest(
    val model: String = "llama3:8b", // ou "phi"
    // ...
)
```

---

### 3. Compilation Gradle Échoue

#### Symptôme
```
Unresolved reference: androidx
Could not resolve dependencies
```

#### Solutions

**A. Sync Gradle**
```
File → Sync Project with Gradle Files
```

**B. Clean & Rebuild**
```
Build → Clean Project
Build → Rebuild Project
```

**C. Invalider le cache**
```
File → Invalidate Caches → Invalidate and Restart
```

**D. Vérifier la version Java**
```bash
java -version
# Doit être JDK 11 ou supérieur
```

Dans Android Studio :
```
File → Project Structure → SDK Location → JDK Location
```

**E. Vérifier gradle.properties**
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
```

---

### 4. Room Database Errors

#### Symptôme
```
Cannot find implementation for AppDatabase
java.lang.IllegalStateException: Room cannot verify the data integrity
```

#### Solutions

**A. Rebuild le projet**
```kotlin
// Si des entités ont changé, incrémenter la version
@Database(
    entities = [...],
    version = 2,  // Était 1
    exportSchema = false
)
```

**B. Fallback destructive migration (dev seulement)**
```kotlin
Room.databaseBuilder(...)
    .fallbackToDestructiveMigration()
    .build()
```

**C. Vérifier les annotations Kapt**
```kotlin
// build.gradle.kts
plugins {
    kotlin("kapt")
}

dependencies {
    kapt("androidx.room:room-compiler:2.6.0")
}
```

---

### 5. Réponses IA Très Lentes

#### Symptôme
Ollama prend plus de 30 secondes pour répondre

#### Diagnostic
```bash
# Vérifier les ressources système
# Windows
taskmgr

# macOS
top

# Linux
htop
```

#### Solutions

**A. Utiliser un modèle plus léger**
```bash
ollama pull llama3:8b
# ou
ollama pull mistral
```

**B. Réduire max_tokens**
```kotlin
data class GenerateRequest(
    val max_tokens: Int = 300, // Au lieu de 600
    // ...
)
```

**C. Augmenter la RAM allouée**
```bash
# Variables d'environnement Ollama
export OLLAMA_MAX_LOADED_MODELS=1
export OLLAMA_NUM_PARALLEL=1
```

**D. Utiliser GPU (si disponible)**
Ollama utilise automatiquement le GPU s'il est disponible (NVIDIA/AMD)

---

### 6. App Crash au Démarrage

#### Symptôme
```
java.lang.RuntimeException: Unable to start activity
```

#### Diagnostic
Lire les logs dans **Logcat** (Android Studio)

#### Solutions Fréquentes

**A. Hilt non configuré**
Vérifier `RiseWellApplication.kt` :
```kotlin
@HiltAndroidApp
class RiseWellApplication : Application()
```

Et dans `AndroidManifest.xml` :
```xml
<application
    android:name=".RiseWellApplication"
    ...>
```

**B. Dépendances Hilt manquantes**
```kotlin
// build.gradle.kts
plugins {
    id("com.google.dagger.hilt.android")
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
}
```

**C. Navigation argument invalide**
Si crash sur navigation vers chat, vérifier que le persona existe :
```kotlin
navController.navigate("chat/${Persona.COACH.name}")
```

---

### 7. Émulateur ne Peut pas Accéder à localhost

#### Symptôme
```
Connection refused to localhost:11434
```

#### Solution
Sur émulateur Android, `localhost` ne fonctionne pas.

**Utiliser l'IP spéciale** :
```kotlin
.baseUrl("http://10.0.2.2:11434/")
```

`10.0.2.2` est l'alias de `localhost` pour l'émulateur Android.

---

### 8. Appareil Physique ne Peut pas se Connecter

#### Diagnostic
```bash
# Trouver l'IP de votre PC
# Windows
ipconfig

# macOS/Linux
ifconfig
ip addr show
```

#### Solution

**A. Utiliser l'IP locale**
```kotlin
.baseUrl("http://192.168.1.XXX:11434/")
```

**B. Vérifier le même réseau WiFi**
Le téléphone et le PC doivent être sur le même réseau local.

**C. Désactiver le pare-feu (temporairement)**
```bash
# Windows
netsh advfirewall set allprofiles state off

# Puis le réactiver après test
netsh advfirewall set allprofiles state on
```

**D. Vérifier qu'Ollama écoute sur toutes les interfaces**
```bash
# Démarrer Ollama en écoutant sur 0.0.0.0
OLLAMA_HOST=0.0.0.0:11434 ollama serve
```

---

## 🐛 Debugging Avancé

### Activer les Logs Détaillés

#### OkHttp Logging
```kotlin
// AppModule.kt
OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Tous les détails
    })
    .build()
```

#### Room Logging
```kotlin
Room.databaseBuilder(...)
    .setQueryCallback({ sqlQuery, bindArgs ->
        Log.d("RoomQuery", "SQL: $sqlQuery")
    }, Executors.newSingleThreadExecutor())
    .build()
```

#### Logcat Filters
Dans Android Studio Logcat :
```
# Filtrer par tag
tag:RiseWell

# Filtrer par niveau
level:error

# Combine
tag:RiseWell level:warn
```

---

## 📋 Checklist de Dépannage

Avant de demander de l'aide, vérifier :

- [ ] Ollama est installé : `ollama --version`
- [ ] Modèle téléchargé : `ollama list`
- [ ] Ollama démarre : `ollama serve`
- [ ] API répond : `curl http://localhost:11434/api/tags`
- [ ] Gradle synchronisé : pas d'erreurs dans `Build`
- [ ] Bon JDK : Java 11+
- [ ] URL correcte dans `AppModule.kt`
- [ ] Permissions dans `AndroidManifest.xml`
- [ ] Logcat ne montre pas d'erreurs critiques

---

## 💡 Astuces de Performance

### 1. Réduire la Latence
```kotlin
// Prompt plus court = réponse plus rapide
val prompt = "Bref conseil fitness: $userMessage"
```

### 2. Utiliser le Cache
```kotlin
// OkHttpClient avec cache
val cacheSize = 10 * 1024 * 1024 // 10 MB
val cache = Cache(context.cacheDir, cacheSize)

OkHttpClient.Builder()
    .cache(cache)
    .build()
```

### 3. Limiter l'Historique
```kotlin
// Ne charger que les 50 derniers messages
@Query("SELECT * FROM messages WHERE conversationId = :id ORDER BY timestamp DESC LIMIT 50")
```

---

## 🔍 Tests Manuels Rapides

### Test 1 : Ollama API
```bash
curl -X POST http://localhost:11434/api/generate \
  -H "Content-Type: application/json" \
  -d '{
    "model": "llama3",
    "prompt": "Say hello",
    "stream": false
  }'
```

**Résultat attendu** : JSON avec `{"response": "Hello! ..."}`

### Test 2 : Base de Données
```kotlin
// Dans un test ou temporairement dans MainActivity
lifecycleScope.launch {
    val profile = UserProfile(
        age = 25,
        weightKg = 70f,
        heightCm = 175,
        activityLevel = ActivityLevel.MODERATE_ACTIVE,
        goal = Goal.IMPROVE_FITNESS
    )
    userProfileDao.insertUserProfile(profile)
    Log.d("Test", "Profile saved!")
}
```

### Test 3 : Navigation
```kotlin
// Vérifier que tous les personas fonctionnent
Persona.values().forEach { persona ->
    navController.navigate("chat/${persona.name}")
    delay(1000)
    navController.popBackStack()
}
```

---

## 📞 Obtenir de l'Aide

### Informations à fournir

Quand vous signalez un bug, incluez :

1. **Version Android** : Settings → About phone
2. **Version de l'app** : Voir `build.gradle.kts`
3. **Logs Logcat** : Copier les erreurs
4. **Étapes pour reproduire** :
   ```
   1. Ouvrir l'app
   2. Cliquer sur Coach
   3. Envoyer un message
   4. → Crash
   ```
5. **Configuration Ollama** :
   ```bash
   ollama --version
   ollama list
   ```

### Où demander de l'aide

- GitHub Issues : [URL du repo]
- Documentation Ollama : https://ollama.ai/docs
- Android Developers : https://developer.android.com

---

## 🎓 Ressources Supplémentaires

### Documentation Officielle
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt/Dagger](https://developer.android.com/training/dependency-injection/hilt-android)
- [Ollama API](https://github.com/ollama/ollama/blob/main/docs/api.md)

### Tutoriels
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
- [StateFlow & SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Coroutines Best Practices](https://developer.android.com/kotlin/coroutines/coroutines-best-practices)

---

**Mis à jour** : Novembre 2025  
**Pour** : RiseWell v1.0.0
