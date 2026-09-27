package com.pokeguide.app.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Retrofit-интерфейс для PokéAPI v2 */
interface PokeService {

    /** Получить список покемонов с пагинацией */
    @GET("pokemon")
    suspend fun fetchList(
        @Query("limit") limit: Int = DEFAULT_LIMIT,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    /** Получить детальную информацию о покемоне по ID */
    @GET("pokemon/{id}")
    suspend fun fetchById(@Path("id") id: Int): PokemonResponse

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val DEFAULT_LIMIT = 40
    }
}
