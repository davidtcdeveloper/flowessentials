package com.davidtiago.flowessentials.finalproject

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class ComputationCache(
    private val dispatcher: CoroutineDispatcher = defaultDispatcherForCache()
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

fun defaultDispatcherForCache() = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
