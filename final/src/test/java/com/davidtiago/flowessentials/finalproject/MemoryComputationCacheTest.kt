package com.davidtiago.flowessentials.finalproject

import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class MemoryComputationCacheTest {
    private val testDispatcher = TestCoroutineDispatcher()

    val memoryComputationCache = MemoryComputationCache(testDispatcher)

    @Nested
    inner class EmptyCacheScenarios {
        @Test
        fun `forNumber 3`() = testDispatcher.runBlockingTest {
            assertNull(
                memoryComputationCache.forNumber(3)
            )
        }

        @Test
        fun `completes then requests again`() = testDispatcher.runBlockingTest {
            memoryComputationCache.computationCompleted(3, 0)

            val cachedNumber = memoryComputationCache.forNumber(3)

            assertEquals(
                expected = 0,
                actual = cachedNumber
            )
        }
    }
}
