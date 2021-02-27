package com.davidtiago.flowessentials.finalproject.progress

sealed class ComputationProgress {
    data class Computing(
        val number: Long,
        val maxProgress: Int,
        val currentProgress: Int,
    ) : ComputationProgress()

    data class Completed(
        val computedNumber: Long,
        val divisors: Long,
    ) : ComputationProgress()
}
