package com.davidtiago.flowessentials.finalproject

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.util.concurrent.Executors

class PrimeNumberComputer(
    private val flowOnDispatcher: CoroutineDispatcher = Dispatchers.Default,
    cacheDispatcher: CoroutineDispatcher =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher(),
) {
    private val cache = ComputationCache(cacheDispatcher)

    fun computeDivisors(number: Long) = flow {
        val range = 2.toLong()..number / 2.toLong()
        var divisorCount: Long = 0
        val cacheForNumber = cache.forNumber(number)
        cacheForNumber?.let {
            Timber.d("Returning cached value")
            emit(
                ComputationProgress.Completed(cacheForNumber)
            )
            return@flow
        }
        emit(
            ComputationProgress.Computing(
                maxProgress = range.count(),
                currentProgress = 0
            )
        )
        val zero: Long = 0
        for (i in range) {
            if (number.rem(i) == zero) {
                Timber.d("isPrimeNo: CAN be divided by %d", i)
                divisorCount += 1
            } else {
                Timber.d("isPrimeNo: CAN'T be divided by %d", i)
            }
            emit(
                ComputationProgress.Computing(
                    maxProgress = range.count(),
                    currentProgress = i.toInt()
                )
            )
        }
        cache.computationCompleted(number, divisorCount)
        emit(ComputationProgress.Completed(divisorCount))
    }.flowOn(flowOnDispatcher)
}
