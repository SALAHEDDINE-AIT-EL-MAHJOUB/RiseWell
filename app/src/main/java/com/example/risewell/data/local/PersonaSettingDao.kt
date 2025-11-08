package com.example.risewell.data.local

import androidx.room.*
import com.example.risewell.data.model.PersonaSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaSettingDao {
    @Query("SELECT * FROM persona_settings WHERE personaName = :personaName")
    fun getPersonaSettings(personaName: String): Flow<PersonaSetting?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonaSetting(setting: PersonaSetting)

    @Update
    suspend fun updatePersonaSetting(setting: PersonaSetting)

    @Query("SELECT * FROM persona_settings")
    fun getAllPersonaSettings(): Flow<List<PersonaSetting>>
}
