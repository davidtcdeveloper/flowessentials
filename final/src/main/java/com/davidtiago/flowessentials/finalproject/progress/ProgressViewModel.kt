package com.davidtiago.flowessentials.finalproject.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val primeNumberComputer: PrimeNumberComputer
) : ViewModel() {
    private val _progress = MutableSharedFlow<ComputationProgress>(replay = 1)
    val progress by lazy { _progress.asSharedFlow() }

    fun computeDivisors(number: Long) {
        viewModelScope.launch {
            primeNumberComputer.computeDivisors(number)
                .collect { computingProgress ->
                    _progress.emit(computingProgress)
                }
        }
    }
}
