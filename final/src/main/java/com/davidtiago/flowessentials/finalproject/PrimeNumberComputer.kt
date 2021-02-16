package com.davidtiago.flowessentials.finalproject

import com.davidtiago.flowessentials.finalproject.di.ComputingDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class PrimeNumberComputer @Inject constructor(
    @ComputingDispatcher private val flowOnDispatcher: CoroutineDispatcher,
    private val cache: ComputationCache,
) {
    fun computeDivisors(number: Long) = flow {
        val range = 2.toLong()..number / 2.toLong()
        var divisorCount: Long = 0
        val cacheForNumber = cache.forNumber(number)
        cacheForNumber?.let {
            Timber.d("Returning cached value")
            emit(
                ComputationProgress.Completed(
                    divisors = cacheForNumber,
                    computedNumber = number
                )
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
        emit(
            ComputationProgress.Completed(
                divisors = divisorCount,
                computedNumber = number
            )
        )
    }.flowOn(flowOnDispatcher)
}
