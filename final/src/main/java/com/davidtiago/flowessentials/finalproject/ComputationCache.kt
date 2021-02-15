package com.davidtiago.flowessentials.finalproject

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ComputationCache(
    private val dispatcher: CoroutineDispatcher,
) {

    private val cache = mutableMapOf<Long, Long>()

    suspend fun forNumber(number: Long): Long? = withContext(dispatcher) {
        delay(50) // Pretend that is going to the database or something like that
        return@withContext cache[number]
    }

    suspend fun computationCompleted(number: Long, divisorCount: Long) = withContext(dispatcher) {
        delay(50) // Pretend that is going to the database or something like that
        cache[number] = divisorCount
    }
}
