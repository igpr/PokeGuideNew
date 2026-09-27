package com.pokeguide.app.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.pokeguide.app.data.local.entity.CaughtPokemonEntity
import com.pokeguide.app.data.local.entity.TrainerProfileEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CaughtPokemonDaoTest {

    private lateinit var db: PokeGuideDatabase
    private var profileId: Long = 0

    @Before
    fun setup() = runTest {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokeGuideDatabase::class.java
        ).allowMainThreadQueries().build()

        profileId = db.trainerProfileDao().insert(
            TrainerProfileEntity(
                name = "Test",
                avatarKey = "red",
                createdAt = 0L
            )
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun upsert_and_observeOne_returnsStatus() = runTest {
        db.caughtPokemonDao().upsert(
            CaughtPokemonEntity(
                profileId = profileId,
                pokemonId = 25,
                status = "CAUGHT",
                note = "Мой первый пикачу",
                updatedAt = 1L
            )
        )

        val entity = db.caughtPokemonDao().observeOne(profileId, 25).first()
        assertThat(entity).isNotNull()
        assertThat(entity!!.status).isEqualTo("CAUGHT")
        assertThat(entity.note).isEqualTo("Мой первый пикачу")
    }

    @Test
    fun delete_removesEntry() = runTest {
        db.caughtPokemonDao().upsert(
            CaughtPokemonEntity(profileId, 1, "WANT", null, 1L)
        )
        db.caughtPokemonDao().delete(profileId, 1)

        assertThat(db.caughtPokemonDao().observeOne(profileId, 1).first()).isNull()
    }

    @Test
    fun observeIdsByStatus_filtersCorrectly() = runTest {
        db.caughtPokemonDao().upsert(CaughtPokemonEntity(profileId, 1, "CAUGHT", null, 1L))
        db.caughtPokemonDao().upsert(CaughtPokemonEntity(profileId, 2, "WANT", null, 2L))
        db.caughtPokemonDao().upsert(CaughtPokemonEntity(profileId, 3, "CAUGHT", null, 3L))

        val caughtIds = db.caughtPokemonDao().observeIdsByStatus(profileId, "CAUGHT").first()
        assertThat(caughtIds).containsExactly(1, 3)
    }

    @Test
    fun cascadeDelete_whenProfileDeleted() = runTest {
        db.caughtPokemonDao().upsert(CaughtPokemonEntity(profileId, 1, "CAUGHT", null, 1L))
        db.trainerProfileDao().deleteById(profileId)

        assertThat(db.caughtPokemonDao().observeOne(profileId, 1).first()).isNull()
    }
}