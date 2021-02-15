package com.davidtiago.flowessentials.finalproject

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PrimeNumberComputerTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val primeNumberComputer = PrimeNumberComputer(
        testDispatcher,
        testDispatcher
    )

    @Test
    fun `computeDivisors should return no divisors for 2`() = testDispatcher.runBlockingTest {
        val collectedDivisors: List<ComputationProgress> =
            primeNumberComputer.computeDivisors(2)
                .toList()

        assertEquals(
            expected = ComputationProgress.Completed(0),
            actual = collectedDivisors.last()
        )
    }
}
