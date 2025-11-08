package com.example.risewell.ui.screens.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.risewell.data.model.Persona

enum class QuickActionType {
    DAILY_PLAN,
    QUICK_TIP,
    WEEKLY_PLAN,
    MOTIVATION
}

data class QuickAction(
    val type: QuickActionType,
    val label: String,
    val icon: ImageVector
)

fun getQuickActionsForPersona(persona: Persona): List<QuickAction> {
    val commonActions = listOf(
        QuickAction(
            type = QuickActionType.QUICK_TIP,
            label = "Conseil rapide",
            icon = Icons.Default.Info
        )
    )

    val personaActions = when (persona) {
        Persona.COACH -> listOf(
            QuickAction(
                type = QuickActionType.DAILY_PLAN,
                label = "Plan du jour",
                icon = Icons.Default.Favorite
            ),
            QuickAction(
                type = QuickActionType.WEEKLY_PLAN,
                label = "Plan hebdo",
                icon = Icons.Default.DateRange
            )
        )
        Persona.NUTRITIONIST -> listOf(
            QuickAction(
                type = QuickActionType.DAILY_PLAN,
                label = "Menu du jour",
                icon = Icons.Default.ShoppingCart
            ),
            QuickAction(
                type = QuickActionType.WEEKLY_PLAN,
                label = "Menu semaine",
                icon = Icons.Default.DateRange
            )
        )
        Persona.MOTIVATOR -> listOf(
            QuickAction(
                type = QuickActionType.MOTIVATION,
                label = "Motivation",
                icon = Icons.Default.Star
            ),
            QuickAction(
                type = QuickActionType.DAILY_PLAN,
                label = "Objectif du jour",
                icon = Icons.Default.CheckCircle
            )
        )
        Persona.CONSULTANT -> listOf(
            QuickAction(
                type = QuickActionType.DAILY_PLAN,
                label = "Analyse du jour",
                icon = Icons.Default.Build
            ),
            QuickAction(
                type = QuickActionType.WEEKLY_PLAN,
                label = "Bilan hebdo",
                icon = Icons.Default.Build
            )
        )
    }

    return personaActions + commonActions
}

fun getPersonaDisplayName(persona: Persona): String {
    return when (persona) {
        Persona.COACH -> "Coach Sportif"
        Persona.NUTRITIONIST -> "Nutritionniste"
        Persona.MOTIVATOR -> "Coach Mental"
        Persona.CONSULTANT -> "Consultant Bien-être"
    }
}

fun getPersonaSubtitle(persona: Persona): String {
    return when (persona) {
        Persona.COACH -> "Entraînement et exercices"
        Persona.NUTRITIONIST -> "Alimentation et nutrition"
        Persona.MOTIVATOR -> "Motivation et objectifs"
        Persona.CONSULTANT -> "Bien-être et progression"
    }
}

fun getWelcomeMessage(persona: Persona): String {
    return when (persona) {
        Persona.COACH -> "Je suis votre coach sportif personnel. Je peux vous aider à créer des plans d'entraînement adaptés et vous donner des conseils d'exercices."
        Persona.NUTRITIONIST -> "Je suis votre nutritionniste. Je peux vous aider avec vos repas, créer des menus équilibrés et vous donner des conseils nutritionnels."
        Persona.MOTIVATOR -> "Je suis votre coach mental. Je suis là pour vous motiver, vous fixer des objectifs et vous aider à rester sur la bonne voie."
        Persona.CONSULTANT -> "Je suis votre consultant bien-être. Je peux vous aider à analyser votre progression et à optimiser votre santé globale."
    }
}

fun getPersonaColor(persona: Persona): Color {
    return when (persona) {
        Persona.COACH -> Color(0xFFE3F2FD)
        Persona.NUTRITIONIST -> Color(0xFFFCE4EC)
        Persona.MOTIVATOR -> Color(0xFFFFF9C4)
        Persona.CONSULTANT -> Color(0xFFE8F5E9)
    }
}
