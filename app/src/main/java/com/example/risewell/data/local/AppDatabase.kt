package com.example.risewell.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.risewell.data.model.Conversation
import com.example.risewell.data.model.Message
import com.example.risewell.data.model.PersonaSetting
import com.example.risewell.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        Conversation::class,
        Message::class,
        PersonaSetting::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun chatDao(): ChatDao
    abstract fun personaSettingDao(): PersonaSettingDao
}
