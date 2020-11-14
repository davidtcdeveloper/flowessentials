package com.davidtiago.flowessentials.finalproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.davidtiago.flowessentials.finalproject.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainActivity : AppCompatActivity() {
    private val cache = ComputationCache()

    private lateinit var binding: ActivityMainBinding

    private lateinit var scope: CoroutineScope

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progress.hide()
        binding.cancelButton.visibility = View.GONE
        binding.computeButton.setOnClickListener {
            with(binding) {
                textView.text = ""
                progress.show()
                computeButton.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
            }
            scope.launch {
                val number = binding.editTextNumber.text.toString().toLong()
                isPrimeNo(number).collect { progress ->
                    when (progress) {
                        is ComputationProgress.Completed -> {
                            if (progress.isPrime) {
                                binding.textView.text = "$number \n is a prime number 👍"
                            } else {
                                binding.textView.text =
                                    "$number \n is NOT a prime number 👎 \n can be divided by ${progress.divisors} other numbers"
                            }
                            binding.computeButton.visibility = View.VISIBLE
                            binding.cancelButton.visibility = View.GONE
                            binding.progress.hide()
                        }
                        is ComputationProgress.Computing -> TODO()
                    }
                }
            }
        }
        binding.cancelButton.setOnClickListener {
            scope.cancel()
            scope = CoroutineScope(Job() + Dispatchers.Main)
            with(binding) {
                progress.hide()
                textView.text = "Computation cancelled"
                computeButton.visibility = View.VISIBLE
                cancelButton.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        scope = CoroutineScope(Job() + Dispatchers.Main)
    }

    override fun onStop() {
        super.onStop()
        scope.cancel()
    }

    private fun isPrimeNo(number: Long) = flow<ComputationProgress> {
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
        for (i in range) {
            yield()
            if (number.rem(i) == 0.toLong()) {
                Log.d("isPrimeNo", "Can be divided by $i")
                divisorCount += 1
            } else {
                Log.d("isPrimeNo", "Can't be divided by $i")
            }
        }
        cache.computationCompleted(number, divisorCount)
        emit(ComputationProgress.Completed(divisorCount))
    }.flowOn(Dispatchers.Default)
}

sealed class ComputationProgress {
    data class Computing(
        val maxProgress: Long,
        val currentProgress: Long,
    ) : ComputationProgress()

    data class Completed(
        val divisors: Long,
    ) : ComputationProgress() {
        val isPrime: Boolean
            get() = divisors > 1
    }
}
