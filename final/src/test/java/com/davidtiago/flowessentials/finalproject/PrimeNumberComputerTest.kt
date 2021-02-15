package com.davidtiago.flowessentials.finalproject

import junit.framework.TestCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals

class PrimeNumberComputerTest : TestCase() {

    private val primeNumberComputer = PrimeNumberComputer()

    @Test
    fun `computeDivisors should return no divisors for 2`() = runBlockingTest {
        val collectedDivisors: List<ComputationProgress> =
            primeNumberComputer.computeDivisors(2)
                .toList()

        assertEquals(
            expected = ComputationProgress.Completed(0),
            actual = collectedDivisors.last()
        )
    }
}
