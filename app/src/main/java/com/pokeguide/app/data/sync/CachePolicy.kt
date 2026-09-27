package com.pokeguide.app.data.sync

object CachePolicy {
    fun isStale(cachedAt: Long, ttlHours: Int, now: Long = System.currentTimeMillis()): Boolean {
        val ageMs = now - cachedAt
        return ageMs > ttlHours * 60L * 60L * 1000L
    }

    fun staleThreshold(ttlHours: Int, now: Long = System.currentTimeMillis()): Long =
        now - ttlHours * 60L * 60L * 1000L

    fun historyThreshold(days: Int, now: Long = System.currentTimeMillis()): Long =
        now - days * 24L * 60L * 60L * 1000L
}