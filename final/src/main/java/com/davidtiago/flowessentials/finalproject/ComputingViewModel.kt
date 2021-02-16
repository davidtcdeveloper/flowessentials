package com.davidtiago.flowessentials.finalproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@HiltViewModel
class ComputingViewModel @Inject constructor(
    private val primeNumberComputer: PrimeNumberComputer
) : ViewModel() {

    private var scope: CoroutineScope? = null

    private val _computingProgress = MutableLiveData<ComputationProgress>()
    val computingProgress: LiveData<ComputationProgress>
        get() = _computingProgress

    fun computeDivisors(number: Long) {
        scope = newScope().apply {
            launch {
                primeNumberComputer.computeDivisors(number)
                    .filter { computationProgress ->
                        computationProgress is ComputationProgress.Completed ||
                                (computationProgress is ComputationProgress.Computing &&
                                        computationProgress.currentProgress.rem(1000) == 0)
                    }
                    .collect { computingProgress ->
                        _computingProgress.postValue(computingProgress)
                    }
            }
        }
    }

    fun cancelComputing() {
        scope?.cancel()
        scope = null
    }

    override fun onCleared() {
        scope?.cancel()
        scope = null
        super.onCleared()
    }

    private fun newScope() = CoroutineScope(Job() + Dispatchers.Main)
}
