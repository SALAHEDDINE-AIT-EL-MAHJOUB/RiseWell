# 🚀 Guide de Démarrage Rapide - RiseWell

## Installation et Configuration (15 minutes)

### Étape 1 : Installer Ollama (5 min)

#### Windows
```bash
# Télécharger depuis https://ollama.ai/download
# Puis exécuter l'installateur

# Vérifier l'installation
ollama --version

# Télécharger le modèle llama3
ollama pull llama3

# Démarrer le serveur
ollama serve
```

#### macOS
```bash
brew install ollama
ollama pull llama3
ollama serve
```

#### Linux
```bash
curl -fsSL https://ollama.ai/install.sh | sh
ollama pull llama3
ollama serve
```

### Étape 2 : Configurer Android Studio (5 min)

1. Ouvrir Android Studio
2. File → Open → Sélectionner le dossier `RiseWell`
3. Attendre la synchronisation Gradle (peut prendre quelques minutes)
4. Tools → SDK Manager → Vérifier que Android SDK 34 est installé

### Étape 3 : Configurer l'URL Ollama (2 min)

**Important** : Modifier `AppModule.kt` selon votre configuration

#### Pour l'émulateur Android :
```kotlin
// app/src/main/java/com/example/risewell/di/AppModule.kt
.baseUrl("http://10.0.2.2:11434/") // ✅ Déjà configuré par défaut
```

#### Pour un appareil physique :

1. Trouver votre IP locale :

**Windows** :
```bash
ipconfig
# Chercher "IPv4 Address" de votre connexion WiFi/Ethernet
```

**macOS/Linux** :
```bash
ifconfig | grep "inet "
# ou
ip addr show
```

2. Modifier `AppModule.kt` :
```kotlin
.baseUrl("http://192.168.1.XXX:11434/") // Remplacer par votre IP
```

### Étape 4 : Compiler et Lancer (3 min)

1. Connecter un appareil Android ou lancer un émulateur
2. Cliquer sur le bouton ▶️ "Run" dans Android Studio
3. Attendre la compilation et l'installation

## 🎯 Premier Lancement

### 1. Créer votre profil

Au premier lancement :
1. Cliquez sur l'icône 👤 (en haut à droite)
2. Remplissez vos informations :
   - **Âge** : 25
   - **Poids** : 75 kg
   - **Taille** : 175 cm
   - **Niveau d'activité** : Modérément actif
   - **Objectif** : Améliorer la forme
3. Appuyez sur "Enregistrer le profil"

### 2. Tester les Personas

Retour à l'écran d'accueil, testez chaque persona :

#### 🏋️ Coach Sportif
Exemple de questions :
- "Donne-moi un plan d'entraînement pour aujourd'hui"
- "Quels exercices pour les abdominaux à la maison ?"
- "Comment améliorer ma forme cardiovasculaire ?"

#### 🥗 Nutritionniste
Exemple de questions :
- "Propose-moi un plan de repas pour aujourd'hui"
- "Quelle recette saine pour le dîner ce soir ?"
- "Comment avoir une alimentation équilibrée pour perdre du poids ?"

#### 💪 Motivateur
Exemple de questions :
- "Motive-moi pour ma séance de sport !"
- "Je me sens découragé, que faire ?"
- "Donne-moi un objectif pour aujourd'hui"

#### 🩺 Consultant Bien-être
Exemple de questions :
- "Fais-moi une analyse de ma situation"
- "Crée-moi un plan sur 4 semaines"
- "Comment améliorer mon bien-être général ?"

### 3. Utiliser les Actions Rapides

Chaque persona propose des **actions rapides** :
- 📋 Plan du jour
- 📅 Plan de la semaine
- 💡 Conseil rapide
- ❤️ Motivation

Cliquez simplement sur une action pour obtenir une réponse immédiate !

## 🔧 Résolution des Problèmes Courants

### Erreur : "Failed to connect to Ollama"

**Cause** : Ollama n'est pas démarré ou l'URL est incorrecte

**Solutions** :
1. Vérifier qu'Ollama est en cours d'exécution :
   ```bash
   # Dans un terminal
   ollama serve
   ```

2. Tester l'API Ollama :
   ```bash
   curl http://localhost:11434/api/generate -d '{
     "model": "llama3",
     "prompt": "Hello"
   }'
   ```

3. Pour émulateur : Utiliser `10.0.2.2` au lieu de `localhost`
4. Pour appareil physique : Vérifier que l'appareil et le PC sont sur le même réseau WiFi

### Erreur : "Model not found"

**Solution** :
```bash
ollama pull llama3
```

### L'app se lance mais ne répond pas

**Solutions** :
1. Vérifier les logs dans Android Studio (Logcat)
2. S'assurer que le modèle `llama3` est téléchargé
3. Redémarrer Ollama :
   ```bash
   # Arrêter Ollama (Ctrl+C)
   # Puis relancer
   ollama serve
   ```

### Réponses très lentes

**Cause** : Le modèle est lourd pour votre machine

**Solutions** :
1. Utiliser un modèle plus léger :
   ```bash
   ollama pull llama3:8b  # Plus rapide
   ```

2. Modifier le modèle dans `OllamaApi.kt` :
   ```kotlin
   data class GenerateRequest(
       val model: String = "llama3:8b",  // Au lieu de "llama3"
       // ...
   )
   ```

## 📊 Vérification de la Configuration

### Checklist avant de commencer :

- [ ] Ollama installé et `ollama --version` fonctionne
- [ ] Modèle téléchargé : `ollama list` montre `llama3`
- [ ] Serveur démarré : `curl http://localhost:11434` répond
- [ ] Android Studio synchronisé sans erreur
- [ ] Émulateur/Appareil connecté et visible dans Android Studio
- [ ] URL correcte dans `AppModule.kt`

### Test rapide de l'API Ollama

```bash
# Windows PowerShell
Invoke-RestMethod -Uri "http://localhost:11434/api/generate" -Method Post -Body '{"model":"llama3","prompt":"Hi"}' -ContentType "application/json"

# macOS/Linux
curl -X POST http://localhost:11434/api/generate \
  -H "Content-Type: application/json" \
  -d '{"model":"llama3","prompt":"Hi"}'
```

Si vous voyez une réponse JSON, Ollama fonctionne correctement ! ✅

## 🎓 Tutoriel Complet

### Scénario : Créer un programme fitness de 4 semaines

1. **Créer votre profil** avec vos objectifs réels
2. **Parler au Consultant** : "Crée-moi un plan sur 4 semaines pour améliorer ma forme"
3. **Obtenir un plan détaillé** avec les étapes semaine par semaine
4. **Parler au Coach** : "Donne-moi le programme d'entraînement de la semaine 1"
5. **Parler au Nutritionniste** : "Quel plan alimentaire pour supporter ce programme ?"
6. **Parler au Motivateur** : "Motive-moi pour commencer !"

### Conseils d'utilisation

- **Soyez précis** : Plus vous donnez de détails, meilleures sont les réponses
- **Utilisez les actions rapides** pour des réponses structurées
- **Créez plusieurs conversations** : Une par semaine d'entraînement, par exemple
- **Mettez à jour votre profil** régulièrement pour des conseils adaptés

## 📱 Prochaines Étapes

Maintenant que tout fonctionne :

1. ⭐ Explorez tous les personas
2. 📝 Testez différents types de questions
3. 💾 Vos conversations sont sauvegardées automatiquement
4. 🔄 Mettez à jour votre profil si vos objectifs changent
5. 🚀 Partagez l'application avec vos amis !

---

**Besoin d'aide ?** Consultez le README.md principal ou ouvrez une issue sur GitHub.
