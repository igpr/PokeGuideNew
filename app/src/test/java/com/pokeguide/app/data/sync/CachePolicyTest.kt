package com.pokeguide.app.data.sync

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CachePolicyTest {

    private val now = 1_700_000_000_000L // фиксированное "сейчас"
    private val hourMs = 60L * 60L * 1000L

    @Test
    fun `fresh cache is not stale`() {
        val cachedAt = now - 2 * hourMs
        assertThat(CachePolicy.isStale(cachedAt, ttlHours = 6, now = now)).isFalse()
    }

    @Test
    fun `cache older than ttl is stale`() {
        val cachedAt = now - 7 * hourMs
        assertThat(CachePolicy.isStale(cachedAt, ttlHours = 6, now = now)).isTrue()
    }

    @Test
    fun `cache at exact ttl boundary is not stale`() {
        val cachedAt = now - 6 * hourMs
        assertThat(CachePolicy.isStale(cachedAt, ttlHours = 6, now = now)).isFalse()
    }

    @Test
    fun `staleThreshold returns now minus ttl`() {
        val threshold = CachePolicy.staleThreshold(ttlHours = 12, now = now)
        assertThat(threshold).isEqualTo(now - 12 * hourMs)
    }

    @Test
    fun `historyThreshold returns now minus days`() {
        val threshold = CachePolicy.historyThreshold(days = 30, now = now)
        assertThat(threshold).isEqualTo(now - 30L * 24 * hourMs)
    }
}