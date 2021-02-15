package com.davidtiago.flowessentials.finalproject

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface ComputationCache {
    suspend fun forNumber(number: Long): Long?

    suspend fun computationCompleted(number: Long, divisorCount: Long)
}

class MemoryComputationCache(
    private val dispatcher: CoroutineDispatcher,
) : ComputationCache {

    private val cache = mutableMapOf<Long, Long>()

    override suspend fun forNumber(number: Long): Long? =
        withContext(dispatcher) {
            return@withContext cache[number]
        }

    override suspend fun computationCompleted(number: Long, divisorCount: Long) =
        withContext(dispatcher) {
            cache[number] = divisorCount
        }
}
