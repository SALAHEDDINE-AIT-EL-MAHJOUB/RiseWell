package com.example.risewell.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val persona: Persona,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val title: String? = null
)

enum class Persona {
    COACH,
    NUTRITIONIST,
    MOTIVATOR,
    CONSULTANT
}
