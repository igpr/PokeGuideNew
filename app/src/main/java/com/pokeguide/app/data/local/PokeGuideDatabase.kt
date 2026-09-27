package com.pokeguide.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pokeguide.app.data.local.dao.CachedPokemonDao
import com.pokeguide.app.data.local.dao.CaughtPokemonDao
import com.pokeguide.app.data.local.dao.CollectionDao
import com.pokeguide.app.data.local.dao.HistoryDao
import com.pokeguide.app.data.local.dao.TrainerProfileDao
import com.pokeguide.app.data.local.entity.CachedPokemonEntity
import com.pokeguide.app.data.local.entity.CaughtPokemonEntity
import com.pokeguide.app.data.local.entity.CollectionEntity
import com.pokeguide.app.data.local.entity.CollectionItemEntity
import com.pokeguide.app.data.local.entity.HistoryEntryEntity
import com.pokeguide.app.data.local.entity.TrainerProfileEntity

@Database(
    entities = [
        TrainerProfileEntity::class,
        CaughtPokemonEntity::class,
        CollectionEntity::class,
        CollectionItemEntity::class,
        HistoryEntryEntity::class,
        CachedPokemonEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class PokeGuideDatabase : RoomDatabase() {
    abstract fun trainerProfileDao(): TrainerProfileDao
    abstract fun caughtPokemonDao(): CaughtPokemonDao
    abstract fun collectionDao(): CollectionDao
    abstract fun historyDao(): HistoryDao
    abstract fun cachedPokemonDao(): CachedPokemonDao
}