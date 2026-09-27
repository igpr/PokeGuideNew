package com.pokeguide.app.model

data class Collection(
    val id: Long,
    val profileId: Long,
    val name: String,
    val createdAt: Long,
    val itemCount: Int = 0
)