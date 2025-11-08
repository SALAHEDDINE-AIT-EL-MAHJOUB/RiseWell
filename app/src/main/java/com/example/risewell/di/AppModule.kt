package com.example.risewell.di

import android.content.Context
import androidx.room.Room
import com.example.risewell.BuildConfig
import com.example.risewell.data.local.*
import com.example.risewell.data.network.GeminiApi
import com.example.risewell.data.network.OllamaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "risewell_db"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(database: AppDatabase): UserProfileDao {
        return database.userProfileDao()
    }

    @Provides
    @Singleton
    fun provideChatDao(database: AppDatabase): ChatDao {
        return database.chatDao()
    }

    @Provides
    @Singleton
    fun providePersonaSettingDao(database: AppDatabase): PersonaSettingDao {
        return database.personaSettingDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:11434/") // Use 10.0.2.2 for Android emulator
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOllamaApi(retrofit: Retrofit): OllamaApi {
        return retrofit.create(OllamaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGeminiApiKey(): String {
        return BuildConfig.GEMINI_API_KEY
    }

    @Provides
    @Singleton
    fun provideGeminiApi(apiKey: String): GeminiApi {
        return GeminiApi(apiKey)
    }
}
