package com.davidtiago.flowessentials.finalproject

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PrimeNumberComputer {
    private val cache = ComputationCache()

    fun computeDivisors(number: Long) = flow {
        val range = 2.toLong()..number / 2.toLong()
        var divisorCount: Long = 0
        val cacheForNumber = cache.forNumber(number)
        cacheForNumber?.let {
            Log.d("isPrimeNo", "Returning cached value")
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
                Log.d("isPrimeNo", "CAN be divided by $i")
                divisorCount += 1
            } else {
                Log.d("isPrimeNo", "CAN'T be divided by $i")
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
    }.flowOn(Dispatchers.Default)
}
