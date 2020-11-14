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

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {
    private val cache = ComputationCache()

    private lateinit var binding: ActivityMainBinding

    private lateinit var scope: CoroutineScope

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.GONE
        binding.cancelButton.visibility = View.GONE
        binding.computeButton.setOnClickListener {
            with(binding) {
                textView.text = ""
                binding.progressBar.visibility = View.VISIBLE
                computeButton.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
            }
            scope.launch {
                val number = binding.editTextNumber.text.toString().toLong()
                isPrimeNo(number).collect { progress ->
                    when (progress) {
                        is ComputationProgress.Completed -> handleCompleted(progress, number)
                        is ComputationProgress.Computing -> handleComputing(progress)
                    }
                }
            }
        }
        binding.cancelButton.setOnClickListener {
            scope.cancel()
            scope = CoroutineScope(Job() + Dispatchers.Main)
            with(binding) {
                binding.progressBar.visibility = View.GONE
                textView.text = "Computation cancelled"
                computeButton.visibility = View.VISIBLE
                cancelButton.visibility = View.GONE
            }
        }
    }

    private fun handleComputing(progress: ComputationProgress.Computing) {
        binding.progressBar.max = progress.maxProgress
        binding.progressBar.progress = progress.currentProgress
    }

    private fun handleCompleted(
        progress: ComputationProgress.Completed,
        number: Long
    ) {
        if (progress.isPrime) {
            binding.textView.text = "$number \n is a prime number 👍"
        } else {
            binding.textView.text =
                "$number \n is NOT a prime number 👎 \n can be divided by ${progress.divisors} other numbers"
        }
        binding.computeButton.visibility = View.VISIBLE
        binding.cancelButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
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
            if (i.rem(1000) == 0.toLong()) {
                emit(
                    ComputationProgress.Computing(
                        maxProgress = range.count(),
                        currentProgress = i.toInt()
                    )
                )
            }
        }
        cache.computationCompleted(number, divisorCount)
        emit(ComputationProgress.Completed(divisorCount))
    }.flowOn(Dispatchers.Default)
}

sealed class ComputationProgress {
    data class Computing(
        val maxProgress: Int,
        val currentProgress: Int,
    ) : ComputationProgress()

    data class Completed(
        val divisors: Long,
    ) : ComputationProgress() {
        val isPrime: Boolean
            get() = divisors > 1
    }
}
