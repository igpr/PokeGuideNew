package com.pokeguide.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "collection_items",
    primaryKeys = ["collectionId", "pokemonId"],
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("collectionId"), Index("pokemonId")]
)
data class CollectionItemEntity(
    val collectionId: Long,
    val pokemonId: Int,
    val addedAt: Long
)