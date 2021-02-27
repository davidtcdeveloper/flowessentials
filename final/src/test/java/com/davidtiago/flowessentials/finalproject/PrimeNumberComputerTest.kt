package com.davidtiago.flowessentials.finalproject

import com.davidtiago.flowessentials.finalproject.progress.ComputationProgress
import com.davidtiago.flowessentials.finalproject.progress.PrimeNumberComputer
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PrimeNumberComputerTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Nested
    inner class ComputeDivisorsPrimeNumbersScenarios {
        private val primeNumberComputer = PrimeNumberComputer(
            flowOnDispatcher = testDispatcher,
            cache = AlwaysEmptyCache()
        )

        @Test
        fun `all emissions for 2`() =
            testDispatcher.runBlockingTest {
                val expectedEmissions = listOf(
                    ComputationProgress.Computing(2, 0, 0),
                    ComputationProgress.Completed(2, 0),
                )
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(2)
                        .toList()

                assertEquals(
                    expected = expectedEmissions,
                    actual = collectedDivisors
                )
            }

        @Test
        fun `last emission for 701`() =
            testDispatcher.runBlockingTest {
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(701)
                        .toList()

                assertEquals(
                    expected = ComputationProgress.Completed(701, 0),
                    actual = collectedDivisors.last()
                )
            }

        @Test
        fun `last emission for 1481`() =
            testDispatcher.runBlockingTest {
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(1481)
                        .toList()

                assertEquals(
                    expected = ComputationProgress.Completed(1481, 0),
                    actual = collectedDivisors.last()
                )
            }

        @Test
        fun `last emission for 2999`() =
            testDispatcher.runBlockingTest {
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(2999)
                        .toList()

                assertEquals(
                    expected = ComputationProgress.Completed(2999, 0),
                    actual = collectedDivisors.last()
                )
            }

        @Test
        fun `last emission for 5189`() =
            testDispatcher.runBlockingTest {
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(5189)
                        .toList()

                assertEquals(
                    expected = ComputationProgress.Completed(5189, 0),
                    actual = collectedDivisors.last()
                )
            }
    }

    @Nested
    inner class ComputeDivisorsNonPrimeNumbersScenarios {
        private val primeNumberComputer = PrimeNumberComputer(
            flowOnDispatcher = testDispatcher,
            cache = AlwaysEmptyCache()
        )

        @Test
        fun `all emissions for 4`() =
            testDispatcher.runBlockingTest {
                val expectedEmissions = listOf(
                    ComputationProgress.Computing(4, 1, 0),
                    ComputationProgress.Computing(4, 1, 2),
                    ComputationProgress.Completed(4, 1),
                )
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(4)
                        .toList()

                assertEquals(
                    expected = expectedEmissions,
                    actual = collectedDivisors
                )
            }

        @Test
        fun `last emission for 10`() =
            testDispatcher.runBlockingTest {
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(10)
                        .toList()

                assertEquals(
                    expected = ComputationProgress.Completed(10, 2),
                    actual = collectedDivisors.last()
                )
            }
    }

    @Nested
    inner class CacheScenarios {
        @Test
        fun `cache for 10`() =
            testDispatcher.runBlockingTest {
                val cache = FixedCache(10, 2)
                val primeNumberComputer = PrimeNumberComputer(
                    flowOnDispatcher = testDispatcher,
                    cache = cache,
                )
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(10)
                        .toList()

                assertEquals(
                    expected = listOf(ComputationProgress.Completed(10, 2)),
                    actual = collectedDivisors
                )
                assertTrue { cache.invoked }
            }

        @Test
        fun `cache for 1481`() =
            testDispatcher.runBlockingTest {
                val cache = FixedCache(1481, 0)
                val primeNumberComputer = PrimeNumberComputer(
                    flowOnDispatcher = testDispatcher,
                    cache = cache,
                )
                val collectedDivisors: List<ComputationProgress> =
                    primeNumberComputer.computeDivisors(1481)
                        .toList()

                assertEquals(
                    expected = listOf(ComputationProgress.Completed(1481, 0)),
                    actual = collectedDivisors
                )
                assertTrue { cache.invoked }
            }
    }

    private inner class AlwaysEmptyCache : ComputationCache {
        override suspend fun forNumber(number: Long): Long? = null

        override suspend fun computationCompleted(number: Long, divisorCount: Long) = Unit
    }

    private inner class FixedCache(
        val forNumber: Long,
        val divisorCount: Long,
    ) : ComputationCache {
        var invoked = false
        override suspend fun forNumber(number: Long): Long {
            return if (forNumber != number) {
                throw IllegalStateException("Invoked for wrong number: $number")
            } else {
                invoked = true
                divisorCount
            }
        }

        override suspend fun computationCompleted(number: Long, divisorCount: Long) =
            throw IllegalStateException("Should not invoke computationCompleted")
    }
}

