package com.pokeguide.app.di

import android.content.Context
import androidx.room.Room
import com.pokeguide.app.data.local.PokeGuideDatabase
import com.pokeguide.app.data.local.dao.CachedPokemonDao
import com.pokeguide.app.data.local.dao.CaughtPokemonDao
import com.pokeguide.app.data.local.dao.CollectionDao
import com.pokeguide.app.data.local.dao.HistoryDao
import com.pokeguide.app.data.local.dao.TrainerProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokeGuideDatabase =
        Room.databaseBuilder(
            context,
            PokeGuideDatabase::class.java,
            "pokeguide.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideTrainerProfileDao(db: PokeGuideDatabase) = db.trainerProfileDao()
    @Provides fun provideCaughtPokemonDao(db: PokeGuideDatabase) = db.caughtPokemonDao()
    @Provides fun provideCollectionDao(db: PokeGuideDatabase) = db.collectionDao()
    @Provides fun provideHistoryDao(db: PokeGuideDatabase) = db.historyDao()
    @Provides fun provideCachedPokemonDao(db: PokeGuideDatabase) = db.cachedPokemonDao()
}