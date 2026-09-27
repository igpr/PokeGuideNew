package com.pokeguide.app.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokeguide.app.model.Ability
import com.pokeguide.app.model.StatValue

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        gson.fromJson(value, object : TypeToken<List<String>>() {}.type) ?: emptyList()

    @TypeConverter
    fun fromStats(value: List<StatValue>): String = gson.toJson(value)

    @TypeConverter
    fun toStats(value: String): List<StatValue> =
        gson.fromJson(value, object : TypeToken<List<StatValue>>() {}.type) ?: emptyList()

    @TypeConverter
    fun fromAbilities(value: List<Ability>): String = gson.toJson(value)

    @TypeConverter
    fun toAbilities(value: String): List<Ability> =
        gson.fromJson(value, object : TypeToken<List<Ability>>() {}.type) ?: emptyList()
}