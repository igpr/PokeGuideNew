package com.pokeguide.app.di

import com.pokeguide.app.api.PokeService
import com.pokeguide.app.data.local.dao.CachedPokemonDao
import com.pokeguide.app.repository.PokemonRepository
import com.pokeguide.app.repository.PokemonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TIMEOUT_SEC = 30L

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides
    @Singleton
    fun providePokeService(client: OkHttpClient): PokeService = Retrofit.Builder()
        .baseUrl(PokeService.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokeService::class.java)

    @Provides
    @Singleton
    fun providePokemonRepository(
        api: PokeService,
        cacheDao: CachedPokemonDao
    ): PokemonRepository = PokemonRepositoryImpl(api, cacheDao)
}