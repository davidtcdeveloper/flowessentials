package com.davidtiago.flowessentials.finalproject

sealed class ComputationProgress {
    data class Computing(
        val maxProgress: Int,
        val currentProgress: Int,
    ) : ComputationProgress()

    data class Completed(
        val divisors: Long,
        val computedNumber: Long,
    ) : ComputationProgress() {
        val isPrime: Boolean
            get() = divisors == 0.toLong()
    }
}
