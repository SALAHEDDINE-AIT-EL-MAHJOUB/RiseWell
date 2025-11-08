package com.example.risewell.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val age: Int,
    val weightKg: Float,
    val heightCm: Int,
    val activityLevel: ActivityLevel,
    val goal: Goal
)

enum class ActivityLevel {
    SEDENTARY,
    LIGHT_ACTIVE,
    MODERATE_ACTIVE,
    VERY_ACTIVE
}

enum class Goal {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_MUSCLE,
    IMPROVE_FITNESS,
    BETTER_HEALTH
}
