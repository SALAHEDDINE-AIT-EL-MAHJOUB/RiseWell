package com.example.risewell.data.local

import androidx.room.TypeConverter
import com.example.risewell.data.model.*
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toActivityLevel(value: String) = enumValueOf<ActivityLevel>(value)

    @TypeConverter
    fun fromActivityLevel(value: ActivityLevel) = value.name

    @TypeConverter
    fun toGoal(value: String) = enumValueOf<Goal>(value)

    @TypeConverter
    fun fromGoal(value: Goal) = value.name

    @TypeConverter
    fun toPersona(value: String) = enumValueOf<Persona>(value)

    @TypeConverter
    fun fromPersona(value: Persona) = value.name

    @TypeConverter
    fun toMessageSender(value: String) = enumValueOf<MessageSender>(value)

    @TypeConverter
    fun fromMessageSender(value: MessageSender) = value.name

    @TypeConverter
    fun toResponseLength(value: String) = enumValueOf<ResponseLength>(value)

    @TypeConverter
    fun fromResponseLength(value: ResponseLength) = value.name
}
