package com.pokeguide.app.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.pokeguide.app.data.local.entity.CollectionEntity
import com.pokeguide.app.data.local.entity.CollectionItemEntity
import com.pokeguide.app.data.local.entity.TrainerProfileEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectionDaoTest {

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
    fun insert_and_observeCollections() = runTest {
        db.collectionDao().insert(
            CollectionEntity(profileId = profileId, name = "Команда мечты", createdAt = 1L)
        )
        val list = db.collectionDao().observeByProfile(profileId).first()
        assertThat(list).hasSize(1)
        assertThat(list[0].name).isEqualTo("Команда мечты")
    }

    @Test
    fun addItem_and_contains() = runTest {
        val colId = db.collectionDao().insert(
            CollectionEntity(profileId = profileId, name = "Test", createdAt = 1L)
        )
        db.collectionDao().addItem(CollectionItemEntity(colId, 25, 0L))

        assertThat(db.collectionDao().contains(colId, 25)).isEqualTo(1)
        assertThat(db.collectionDao().contains(colId, 1)).isEqualTo(0)
    }

    @Test
    fun addItem_ignoresDuplicate() = runTest {
        val colId = db.collectionDao().insert(
            CollectionEntity(profileId = profileId, name = "Test", createdAt = 1L)
        )
        db.collectionDao().addItem(CollectionItemEntity(colId, 25, 0L))
        db.collectionDao().addItem(CollectionItemEntity(colId, 25, 0L))

        assertThat(db.collectionDao().observeItems(colId).first()).hasSize(1)
    }

    @Test
    fun cascadeDelete_removesItems() = runTest {
        val colId = db.collectionDao().insert(
            CollectionEntity(profileId = profileId, name = "Test", createdAt = 1L)
        )
        db.collectionDao().addItem(CollectionItemEntity(colId, 25, 0L))
        db.collectionDao().delete(colId)

        assertThat(db.collectionDao().observeItems(colId).first()).isEmpty()
    }
}