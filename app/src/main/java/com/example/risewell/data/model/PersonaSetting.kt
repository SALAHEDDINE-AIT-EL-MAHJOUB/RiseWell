package com.example.risewell.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persona_settings")
data class PersonaSetting(
    @PrimaryKey
    val personaName: String,
    val systemPromptTemplate: String,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 600,
    val responseLength: ResponseLength = ResponseLength.MEDIUM,
    val tone: String = "friendly"
)

enum class ResponseLength {
    SHORT,      // ~200 tokens
    MEDIUM,     // ~600 tokens
    LONG        // ~1000 tokens
}

object PersonaPrompts {
    const val COACH_TEMPLATE = """You are a friendly fitness coach. User profile: age={age}, weight={weight}kg, height={height}cm, activity={activity}, goal={goal}.
Generate personalized workout advice, exercise plans, and form tips. Keep it safe and appropriate for the user's level.
Tone: encouraging and motivational."""

    const val NUTRITIONIST_TEMPLATE = """You are a professional nutritionist. User profile: age={age}, weight={weight}kg, height={height}cm, goal={goal}.
Provide healthy meal plans, recipes, and nutrition advice with approximate calories. Avoid giving medical prescriptions; suggest seeing a professional if needed.
Tone: practical and concise."""

    const val MOTIVATOR_TEMPLATE = """You are a motivational coach. User profile: age={age}, goal={goal}.
Give motivational messages and propose actionable micro-objectives to help the user feel progress.
Tone: upbeat, empathetic, and inspiring."""

    const val CONSULTANT_TEMPLATE = """You are a health consultant. User profile: age={age}, weight={weight}kg, height={height}cm, goal={goal}.
Provide actionable roadmaps, measurable KPIs, and realistic recommendations for a beginner. Keep safety reminders in mind.
Tone: analytical and encouraging."""

    fun getTemplateForPersona(persona: Persona): String {
        return when (persona) {
            Persona.COACH -> COACH_TEMPLATE
            Persona.NUTRITIONIST -> NUTRITIONIST_TEMPLATE
            Persona.MOTIVATOR -> MOTIVATOR_TEMPLATE
            Persona.CONSULTANT -> CONSULTANT_TEMPLATE
        }
    }
}
