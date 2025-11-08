# 📝 Commandes Utiles - RiseWell

Guide de référence rapide pour le développement de RiseWell.

## 🔧 Ollama

### Installation
```bash
# Windows (PowerShell Admin)
winget install ollama

# macOS
brew install ollama

# Linux
curl -fsSL https://ollama.ai/install.sh | sh
```

### Gestion des Modèles
```bash
# Lister les modèles disponibles
ollama list

# Télécharger un modèle
ollama pull llama3
ollama pull llama3:8b      # Version plus légère
ollama pull mistral        # Alternative

# Supprimer un modèle
ollama rm llama3

# Informations sur un modèle
ollama show llama3
```

### Serveur Ollama
```bash
# Démarrer le serveur (port 11434 par défaut)
ollama serve

# Démarrer sur un port spécifique
OLLAMA_HOST=0.0.0.0:11434 ollama serve

# Démarrer sur toutes les interfaces (pour appareil physique)
OLLAMA_HOST=0.0.0.0 ollama serve

# Variables d'environnement utiles
export OLLAMA_MAX_LOADED_MODELS=1    # Limite les modèles en mémoire
export OLLAMA_NUM_PARALLEL=1         # Requêtes simultanées
```

### Test de l'API
```bash
# Windows PowerShell
Invoke-RestMethod -Uri "http://localhost:11434/api/tags" -Method Get

# macOS/Linux
curl http://localhost:11434/api/tags

# Test de génération
curl -X POST http://localhost:11434/api/generate \
  -H "Content-Type: application/json" \
  -d '{
    "model": "llama3",
    "prompt": "Hello, how are you?",
    "stream": false
  }'
```

---

## 📱 Android Studio

### Gradle
```bash
# Synchroniser Gradle
./gradlew sync

# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Installer sur appareil
./gradlew installDebug

# Linter
./gradlew lint

# Tests unitaires
./gradlew test

# Tests instrumentés
./gradlew connectedAndroidTest
```

### ADB (Android Debug Bridge)
```bash
# Lister les appareils
adb devices

# Installer APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Désinstaller
adb uninstall com.example.risewell

# Logs en temps réel
adb logcat

# Filtrer les logs
adb logcat | grep "RiseWell"
adb logcat *:E  # Seulement erreurs
adb logcat -s TAG:V  # Filtrer par tag

# Clear logs
adb logcat -c

# Shell sur l'appareil
adb shell

# Copier fichier vers appareil
adb push local_file /sdcard/

# Copier fichier depuis appareil
adb pull /sdcard/file local_path

# Redémarrer appareil
adb reboot

# Tuer le serveur ADB
adb kill-server
adb start-server
```

### Émulateur
```bash
# Lister les AVDs
emulator -list-avds

# Démarrer un émulateur
emulator -avd Pixel_6_API_34

# Démarrer avec wiping data
emulator -avd Pixel_6_API_34 -wipe-data

# Démarrer avec GPU
emulator -avd Pixel_6_API_34 -gpu host
```

---

## 🗄️ Base de Données (Room)

### Inspection via ADB
```bash
# Accéder au shell de l'appareil
adb shell

# Se placer dans le dossier de l'app
cd /data/data/com.example.risewell/databases/

# Lister les fichiers
ls -la

# Ouvrir la base de données
sqlite3 risewell_db

# Commandes SQLite
.tables                          # Lister les tables
.schema user_profiles            # Voir le schéma
SELECT * FROM user_profiles;     # Query
.exit                            # Quitter
```

### Export de la base
```bash
# Extraire la DB vers l'ordinateur
adb pull /data/data/com.example.risewell/databases/risewell_db ./

# Ouvrir avec SQLite (Windows)
sqlite3.exe risewell_db

# Ouvrir avec SQLite (macOS/Linux)
sqlite3 risewell_db
```

---

## 🌐 Réseau

### Trouver l'IP Locale
```bash
# Windows
ipconfig
# Chercher "IPv4 Address"

# macOS
ifconfig | grep "inet "
ipconfig getifaddr en0

# Linux
ip addr show
ifconfig
hostname -I
```

### Test de Connectivité
```bash
# Ping depuis l'appareil vers le PC
adb shell ping -c 4 192.168.x.x

# Test port Ollama
telnet localhost 11434
nc -zv localhost 11434

# curl depuis l'appareil (nécessite root ou termux)
adb shell curl http://192.168.x.x:11434/api/tags
```

### Pare-feu
```bash
# Windows - Autoriser Ollama
netsh advfirewall firewall add rule name="Ollama" dir=in action=allow protocol=TCP localport=11434

# macOS
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --add /usr/local/bin/ollama

# Linux (ufw)
sudo ufw allow 11434/tcp
```

---

## 🔍 Debugging

### Logcat Avancé
```bash
# Filtres utiles
adb logcat | grep -E "RiseWell|Ollama|Room|Hilt"

# Par niveau
adb logcat *:E *:W  # Erreurs et warnings seulement

# Par tag
adb logcat -s RiseWellRepository:V

# Save to file
adb logcat > logs.txt

# Clear and start fresh
adb logcat -c && adb logcat
```

### Profiling
```bash
# CPU profiling
adb shell am profile start com.example.risewell /sdcard/profile.trace
# ... utiliser l'app ...
adb shell am profile stop com.example.risewell
adb pull /sdcard/profile.trace

# Memory dump
adb shell am dumpheap com.example.risewell /sdcard/heap.hprof
adb pull /sdcard/heap.hprof
```

---

## 🧪 Tests

### Tests Unitaires
```bash
# Lancer tous les tests
./gradlew test

# Tests spécifiques
./gradlew test --tests ChatViewModelTest
./gradlew test --tests "*Repository*"

# Avec rapport
./gradlew test --info

# Ouvrir le rapport
open app/build/reports/tests/testDebugUnitTest/index.html
```

### Tests Instrumentés
```bash
# Lancer sur appareil/émulateur connecté
./gradlew connectedAndroidTest

# Tests spécifiques
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.risewell.ChatScreenTest
```

---

## 🚀 Build & Release

### Debug Build
```bash
# Build
./gradlew assembleDebug

# Installer
./gradlew installDebug

# Build + Install + Run
./gradlew installDebug && adb shell am start -n com.example.risewell/.MainActivity
```

### Release Build
```bash
# Générer un keystore (première fois)
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias

# Build release
./gradlew assembleRelease

# APK signé se trouve dans:
# app/build/outputs/apk/release/app-release.apk
```

### APK Analyzer
```bash
# Analyser la taille de l'APK
./gradlew :app:buildApkAnalyzer

# Dans Android Studio
# Build → Analyze APK → Sélectionner app-debug.apk
```

---

## 📦 Nettoyage

### Gradle Cache
```bash
# Nettoyer le build
./gradlew clean

# Supprimer le cache Gradle
rm -rf ~/.gradle/caches/

# Supprimer build folders
find . -type d -name "build" -exec rm -rf {} +
```

### Android Studio Cache
```
File → Invalidate Caches → Invalidate and Restart
```

---

## 🔄 Git (Workflow)

### Commits Standards
```bash
# Feature
git commit -m "feat: add quick actions to chat screen"

# Fix
git commit -m "fix: resolve ollama connection timeout"

# Documentation
git commit -m "docs: update README with installation steps"

# Refactor
git commit -m "refactor: extract repository pattern"

# Test
git commit -m "test: add unit tests for ChatViewModel"
```

### Branches
```bash
# Créer une branche feature
git checkout -b feature/add-notifications

# Push vers remote
git push -u origin feature/add-notifications

# Merge dans main
git checkout main
git merge feature/add-notifications

# Supprimer la branche
git branch -d feature/add-notifications
```

---

## 🎯 Raccourcis Android Studio

### Essentiels
- `Ctrl+N` / `Cmd+O` : Chercher une classe
- `Ctrl+Shift+N` / `Cmd+Shift+O` : Chercher un fichier
- `Ctrl+Alt+L` / `Cmd+Option+L` : Formater le code
- `Ctrl+/` / `Cmd+/` : Commenter/décommenter
- `Ctrl+D` / `Cmd+D` : Dupliquer la ligne
- `Ctrl+Y` / `Cmd+Delete` : Supprimer la ligne
- `Alt+Enter` / `Option+Return` : Quick fix
- `Ctrl+Click` / `Cmd+Click` : Go to definition

### Refactoring
- `Shift+F6` : Renommer
- `Ctrl+Alt+M` / `Cmd+Option+M` : Extraire méthode
- `Ctrl+Alt+V` / `Cmd+Option+V` : Extraire variable
- `Ctrl+Alt+P` / `Cmd+Option+P` : Extraire paramètre

### Run/Debug
- `Shift+F10` : Run
- `Shift+F9` : Debug
- `Ctrl+F5` / `Cmd+R` : Rerun
- `Ctrl+F2` : Stop

---

## 📚 Ressources Rapides

### Documentation
```bash
# Ouvrir la doc locale
./gradlew :app:dokkaHtml
open app/build/dokka/html/index.html
```

### Liens Utiles
- [Ollama API](https://github.com/ollama/ollama/blob/main/docs/api.md)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room](https://developer.android.com/training/data-storage/room)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**Dernière mise à jour** : Novembre 2025  
**Version** : 1.0.0
