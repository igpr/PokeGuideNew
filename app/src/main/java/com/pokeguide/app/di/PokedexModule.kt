package com.pokeguide.app.di

import com.pokeguide.app.repository.PokedexRepository
import com.pokeguide.app.repository.PokedexRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PokedexModule {
    @Binds
    @Singleton
    abstract fun bindPokedexRepository(impl: PokedexRepositoryImpl): PokedexRepository
}