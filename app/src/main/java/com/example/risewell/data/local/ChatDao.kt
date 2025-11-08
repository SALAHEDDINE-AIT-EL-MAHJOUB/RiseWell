package com.example.risewell.data.local

import androidx.room.*
import com.example.risewell.data.model.Conversation
import com.example.risewell.data.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    // Conversation operations
    @Insert
    suspend fun insertConversation(conversation: Conversation): Long

    @Query("SELECT * FROM conversations WHERE id = :id")
    fun getConversationById(id: Long): Flow<Conversation?>

    @Query("SELECT * FROM conversations ORDER BY createdAt DESC")
    fun getAllConversations(): Flow<List<Conversation>>

    @Query("SELECT * FROM conversations WHERE persona = :persona ORDER BY createdAt DESC")
    fun getConversationsByPersona(persona: String): Flow<List<Conversation>>

    @Delete
    suspend fun deleteConversation(conversation: Conversation)

    // Message operations
    @Insert
    suspend fun insertMessage(message: Message): Long

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesByConversation(conversationId: Long): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getMessageById(id: Long): Message?

    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversation(conversationId: Long)

    @Delete
    suspend fun deleteMessage(message: Message)

    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId")
    suspend fun getMessageCount(conversationId: Long): Int
}
