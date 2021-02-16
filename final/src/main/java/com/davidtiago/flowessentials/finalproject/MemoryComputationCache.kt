package com.davidtiago.flowessentials.finalproject

import com.davidtiago.flowessentials.finalproject.di.CacheDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ComputationCache {
    suspend fun forNumber(number: Long): Long?

    suspend fun computationCompleted(number: Long, divisorCount: Long)
}

class MemoryComputationCache @Inject constructor(
    @CacheDispatcher private val dispatcher: CoroutineDispatcher,
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
