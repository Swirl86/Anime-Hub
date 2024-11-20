package com.swirl.anime_hub.di

import android.app.Application
import androidx.room.Room
import com.swirl.anime_hub.data.local.AnimeDao
import com.swirl.anime_hub.data.local.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the application database with optional migration handling
     */
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AnimeDatabase {
        return Room.databaseBuilder(app, AnimeDatabase::class.java, "anime_database")
            //.addMigrations(MIGRATION_1_2) // Add migration logic as needed
            .build()
    }

    /**
     * Provides the AnimeDao for database access
     */
    @Provides
    fun provideAnimeDao(database: AnimeDatabase): AnimeDao {
        return database.animeDao()
    }
}
