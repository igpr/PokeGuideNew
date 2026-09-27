package com.pokeguide.app.di

import com.pokeguide.app.repository.TrainerProfileRepository
import com.pokeguide.app.repository.TrainerProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindTrainerProfileRepository(
        impl: TrainerProfileRepositoryImpl
    ): TrainerProfileRepository
}