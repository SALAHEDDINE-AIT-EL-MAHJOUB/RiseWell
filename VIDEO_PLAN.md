# 🎬 Plan de la Vidéo et Script Audio - RiseWell

Ce document contient le plan détaillé pour la réalisation de la vidéo explicative du projet ainsi que le script audio complet.

## 📋 Partie 1 : Structure de la Vidéo (5-7 minutes)

### **Titre**
"RiseWell - Votre Coach Santé IA Personnel | Application Mobile Android"

### **1. INTRO (30 secondes)**
*   **Visuel** : Logo RiseWell animé ou face caméra.
*   **Narration** : "Bonjour ! Aujourd'hui, je vous présente RiseWell, une application mobile Android innovante qui combine l'intelligence artificielle et le coaching santé personnalisé. Cette application offre 4 assistants IA spécialisés pour vous accompagner dans votre transformation santé."

### **2. PROBLÉMATIQUE (45 secondes)**
*   **Visuel** : Texte à l'écran ("Difficulté à maintenir une routine", "Manque de suivi", "Besoin de motivation").
*   **Narration** : "Beaucoup de personnes abandonnent leurs objectifs santé par manque d'accompagnement personnalisé. RiseWell résout ce problème avec des coachs IA disponibles 24/7."

### **3. SOLUTION - ARCHITECTURE (1 minute)**
*   **Visuel** : Schéma d'architecture (Android/Compose -> MVVM -> Room/Retrofit -> Gemini/Ollama).
*   **Narration** : "RiseWell utilise une architecture MVVM moderne avec Jetpack Compose pour une interface fluide, Room pour la persistance locale, et l'API Gemini/Ollama pour l'intelligence artificielle."

### **4. DÉMONSTRATION - ÉCRAN D'ACCUEIL (1 minute)**
*   **Visuel** : Démo de `HomeScreen.kt`. Montrer le header, les 4 cartes personas animées (Coach, Nutritionniste, Motivateur, Consultant).
*   **Narration** : "L'écran d'accueil présente 4 assistants spécialisés. Chaque carte est animée avec des gradients modernes et réagit aux interactions utilisateur."

### **5. DÉMONSTRATION - PROFIL UTILISATEUR (1 minute)**
*   **Visuel** : Démo de `ProfileScreen.kt`. Formulaire, calcul IMC, objectifs.
*   **Narration** : "Le profil utilisateur permet de personnaliser l'expérience. Les données sont stockées localement avec Room Database et utilisées pour adapter les conseils de l'IA."

### **6. DÉMONSTRATION - CHAT IA (1.5 minutes)**
*   **Visuel** : Démo de `ChatScreen.kt`. Conversation avec le Coach Sportif et le Nutritionniste.
*   **Narration** : "Chaque assistant utilise l'API Gemini pour générer des réponses contextuelles basées sur votre profil. Les conversations sont sauvegardées localement."

### **7. TECHNOLOGIES UTILISÉES (45 secondes)**
*   **Visuel** : Liste des technos (Kotlin, Compose, Room, Hilt, Retrofit, Gemini).
*   **Narration** : "RiseWell exploite les technologies Android les plus récentes pour garantir performance, maintenabilité et expérience utilisateur optimale."

### **8. CONCLUSION (30 secondes)**
*   **Visuel** : Récapitulatif et appel à l'action.
*   **Narration** : "RiseWell démontre comment combiner IA et développement mobile pour créer une expérience santé personnalisée. Merci de votre attention !"

---

## 🎙️ Partie 2 : Script Audio Détaillé

### **[INTRO - 30 secondes]**
"Bonjour et bienvenue ! Aujourd'hui, je vais vous présenter RiseWell, une application mobile Android révolutionnaire qui combine intelligence artificielle et coaching santé personnalisé. RiseWell, c'est votre assistant santé disponible 24 heures sur 24, 7 jours sur 7, directement dans votre poche. Prêts ? C'est parti !"

### **[SECTION 1 : VUE D'ENSEMBLE - 1 minute]**
"RiseWell est bien plus qu'une simple application de santé. C'est un écosystème complet qui vous accompagne dans votre transformation personnelle. L'application propose QUATRE assistants IA spécialisés :
*   **Le COACH SPORTIF** 🏋️ : Votre expert en fitness pour des programmes personnalisés.
*   **Le NUTRITIONNISTE** 🥗 : Votre conseiller alimentaire pour des repas équilibrés.
*   **Le MOTIVATEUR** 💪 : Votre source d'inspiration quotidienne.
*   **Le CONSULTANT SANTÉ** 🩺 : Votre expert en bien-être global.
Chaque assistant utilise l'intelligence artificielle de Google Gemini pour vous fournir des réponses personnalisées."

### **[SECTION 2 : ÉCRAN D'ACCUEIL - 1 minute 30 secondes]**
"Commençons par l'écran d'accueil. En haut, un header moderne avec le logo RiseWell et votre profil. Juste en dessous, 'Choisissez votre assistant personnel'.
Les cartes personas sont magnifiques en format grille 2x2.
*   **Coach Sportif** : Dégradé violet profond.
*   **Nutritionniste** : Dégradé vert rafraîchissant.
*   **Motivateur** : Dégradé rose-rouge dynamique.
*   **Consultant** : Dégradé bleu cyan apaisant.
Chaque carte est animée et invite à l'interaction."

### **[SECTION 3 : ANIMATIONS ET INTERACTIONS - 45 secondes]**
"Les animations rendent RiseWell vivant ! Quand vous appuyez sur une carte, elle se contracte avec un effet de ressort (Spring Animation). L'écran utilise un dégradé de fond subtil. Tout suit les principes de Material Design 3 pour une expérience fluide."

### **[SECTION 4 : NAVIGATION ET PROFIL - 1 minute]**
"La navigation est simple. Via le bouton PROFIL, vous renseignez nom, âge, poids, taille et objectifs. L'application calcule automatiquement votre IMC et stocke tout localement via Room Database. Ces données permettent aux IA de vous connaître parfaitement."

### **[SECTION 5 : INTERFACE DE CHAT - 1 minute 30 secondes]**
"L'interface de chat est le cœur de l'app. Historique fluide, bulles colorées, et réponses en temps réel avec un effet de frappe naturel.
Exemple : Demandez au Coach 'Je veux perdre 5kg'. L'IA analyse votre profil et génère un plan complet instantanément. Tout est sauvegardé pour consultation ultérieure."

### **[SECTION 6 : TECHNOLOGIES - 1 minute 30 secondes]**
"Côté technique :
*   **Kotlin** & **Jetpack Compose** pour l'UI moderne.
*   **Architecture MVVM** pour la séparation des soucis.
*   **Room Database** pour la persistance locale.
*   **Retrofit & Coroutines** pour les appels réseau non-bloquants.
*   **Hilt** pour l'injection de dépendances.
*   **Google Gemini API** pour l'intelligence artificielle."

### **[CONCLUSION - 45 secondes]**
"Pour conclure, RiseWell prouve que la technologie peut rendre la santé accessible et personnalisée. C'est un projet complet alliant UI moderne, architecture solide et IA. Merci d'avoir écouté, et bonne santé avec RiseWell !"
