package com.davidtiago.flowessentials.finalproject

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PrimeNumberComputerTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val primeNumberComputer = PrimeNumberComputer(
        testDispatcher,
        testDispatcher
    )

    @Nested
    inner class ComputeDivisorsPrimeNumbersScenarios {
        @Test
        fun `all emissions for 2`() =
            testDispatcher.runBlockingTest {
                val expectedEmissions = listOf(
                    ComputationProgress.Computing(0, 0),
                    ComputationProgress.Completed(0),
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
                    expected = ComputationProgress.Completed(0),
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
                    expected = ComputationProgress.Completed(0),
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
                    expected = ComputationProgress.Completed(0),
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
                    expected = ComputationProgress.Completed(0),
                    actual = collectedDivisors.last()
                )
            }
    }

    @Nested
    inner class ComputeDivisorsNonPrimeNumbersScenarios {
        @Test
        fun `all emissions for 4`() =
            testDispatcher.runBlockingTest {
                val expectedEmissions = listOf(
                    ComputationProgress.Computing(1, 0),
                    ComputationProgress.Computing(1, 2),
                    ComputationProgress.Completed(1),
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
                    expected = ComputationProgress.Completed(2),
                    actual = collectedDivisors.last()
                )
            }
    }
}
