# Test de l'API Gemini

## Étapes pour tester Gemini

### 1. Vérifier la clé API
La clé API est configurée dans `gradle.properties`:
```
GEMINI_API_KEY=AIzaSyDISCTKijmm787iLDWl7ZUbI52itIJwRD0
```

### 2. Vérifier les logs
Lors de l'utilisation de l'app, consultez les logs Logcat dans Android Studio pour voir:
- `🔄 Gemini API: Envoi de la requête...` - Requête envoyée
- `✅ Gemini API: Réponse reçue` - Succès
- `❌ Gemini API Error:` - Erreur avec détails
- `⚠️ Gemini API: Clé API non configurée` - Clé manquante

### 3. Tester dans l'application
1. Lancez l'application
2. Assurez-vous qu'Ollama n'est PAS en cours d'exécution (pour forcer l'utilisation de Gemini)
3. Sélectionnez un persona (Coach, Nutritionniste, etc.)
4. Envoyez un message
5. Observez la réponse

### 4. Problèmes courants

#### La clé API n'est pas valide
- Vérifiez que la clé dans `gradle.properties` est correcte
- Générez une nouvelle clé sur: https://makersuite.google.com/app/apikey
- Après modification, exécutez: `.\gradlew.bat clean`

#### Erreur de connexion
- Vérifiez votre connexion Internet
- L'API Gemini nécessite une connexion Internet active

#### Quota dépassé
- L'API Gemini gratuite a des limites
- Consultez: https://ai.google.dev/pricing

### 5. Commandes utiles

```powershell
# Nettoyer et rebuild
.\gradlew.bat clean
.\gradlew.bat :app:assembleDebug

# Installer sur l'appareil
.\gradlew.bat :app:installDebug

# Voir les logs en temps réel
adb logcat | Select-String "Gemini"
```

### 6. Configuration de secours

Si Gemini ne fonctionne toujours pas, vous pouvez utiliser Ollama localement:

1. Installez Ollama: https://ollama.ai
2. Téléchargez le modèle: `ollama pull llama3`
3. Lancez Ollama: `ollama serve`
4. L'app utilisera automatiquement Ollama au lieu de Gemini

## État actuel
✅ Clé API configurée dans gradle.properties
✅ Logs de débogage ajoutés
✅ Fallback Ollama → Gemini configuré
✅ Vérification de la clé API améliorée
