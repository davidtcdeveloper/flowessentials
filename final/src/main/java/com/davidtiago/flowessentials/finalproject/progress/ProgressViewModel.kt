package com.davidtiago.flowessentials.finalproject.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidtiago.flowessentials.finalproject.ComputationProgress
import com.davidtiago.flowessentials.finalproject.PrimeNumberComputer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val primeNumberComputer: PrimeNumberComputer
) : ViewModel() {

    private val _computingProgress = MutableLiveData<ComputationProgress>()
    val computingProgress: LiveData<ComputationProgress>
        get() = _computingProgress

    fun computeDivisors(number: Long) {
        viewModelScope.launch {
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
