package com.pokeguide.app.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.pokeguide.app.data.local.entity.HistoryEntryEntity
import com.pokeguide.app.data.local.entity.TrainerProfileEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryDaoTest {

    private lateinit var db: PokeGuideDatabase
    private var profileId: Long = 0

    @Before
    fun setup() = runTest {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokeGuideDatabase::class.java
        ).allowMainThreadQueries().build()

        profileId = db.trainerProfileDao().insert(
            TrainerProfileEntity(name = "Test", avatarKey = "red", createdAt = 0L)
        )
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun upsert_replacesSamePokemon() = runTest {
        db.historyDao().upsert(HistoryEntryEntity(profileId, 1, 100L))
        db.historyDao().upsert(HistoryEntryEntity(profileId, 1, 200L))

        val list = db.historyDao().observeRecent(profileId).first()
        assertThat(list).hasSize(1)
        assertThat(list[0].viewedAt).isEqualTo(200L)
    }

    @Test
    fun observeRecent_orderedByViewedAtDesc() = runTest {
        db.historyDao().upsert(HistoryEntryEntity(profileId, 1, 100L))
        db.historyDao().upsert(HistoryEntryEntity(profileId, 2, 200L))
        db.historyDao().upsert(HistoryEntryEntity(profileId, 3, 150L))

        val list = db.historyDao().observeRecent(profileId).first()
        assertThat(list.map { it.pokemonId }).containsExactly(2, 3, 1).inOrder()
    }

    @Test
    fun deleteOlderThan_removesStale() = runTest {
        db.historyDao().upsert(HistoryEntryEntity(profileId, 1, 100L))
        db.historyDao().upsert(HistoryEntryEntity(profileId, 2, 500L))

        db.historyDao().deleteOlderThan(300L)
        val list = db.historyDao().observeRecent(profileId).first()
        assertThat(list.map { it.pokemonId }).containsExactly(2)
    }

    @Test
    fun clearProfile_removesAllForProfile() = runTest {
        db.historyDao().upsert(HistoryEntryEntity(profileId, 1, 100L))
        db.historyDao().upsert(HistoryEntryEntity(profileId, 2, 200L))
        db.historyDao().clearProfile(profileId)

        assertThat(db.historyDao().observeRecent(profileId).first()).isEmpty()
    }
}