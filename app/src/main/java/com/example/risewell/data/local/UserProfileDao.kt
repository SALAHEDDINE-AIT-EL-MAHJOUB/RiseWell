package com.example.risewell.data.local

import androidx.room.*
import com.example.risewell.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE id = :id")
    suspend fun getUserProfileById(id: Long): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile): Long

    @Update
    suspend fun updateUserProfile(profile: UserProfile)

    @Delete
    suspend fun deleteUserProfile(profile: UserProfile)

    @Query("DELETE FROM user_profiles")
    suspend fun deleteAllProfiles()

    @Query("SELECT COUNT(*) FROM user_profiles")
    suspend fun getProfileCount(): Int
}
