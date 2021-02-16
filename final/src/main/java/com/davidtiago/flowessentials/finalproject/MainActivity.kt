package com.davidtiago.flowessentials.finalproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.davidtiago.flowessentials.finalproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: ComputingViewModel by viewModels()

    @Inject
    lateinit var primeNumberComputer: PrimeNumberComputer

    private lateinit var binding: ActivityMainBinding

    private lateinit var scope: CoroutineScope

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adjustViewForReadyToComputeState()
        binding.computeButton.setOnClickListener {
            scope.launch {
                val number = binding.editTextNumber.text.toString().toLong()
                binding.textView.text = ""
                primeNumberComputer.computeDivisors(number)
                    .filter { computationProgress ->
                        computationProgress is ComputationProgress.Completed ||
                                (computationProgress is ComputationProgress.Computing &&
                                        computationProgress.currentProgress.rem(1000) == 0)
                    }
                    .collect { progress ->
                        when (progress) {
                            is ComputationProgress.Completed -> handleCompleted(progress, number)
                            is ComputationProgress.Computing -> handleComputing(progress)
                        }
                    }
            }
        }
        binding.cancelButton.setOnClickListener {
            scope.cancel()
            scope = newScope()
            binding.textView.text = "Computation cancelled"
            adjustViewForReadyToComputeState()
        }
    }

    private fun handleComputing(progress: ComputationProgress.Computing) {
        scope.launch {

            with(binding) {
                progressBar.visibility = View.VISIBLE
                computeButton.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
                progressBar.max = progress.maxProgress
                progressBar.progress = progress.currentProgress
            }
        }
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
        adjustViewForReadyToComputeState()
    }

    private fun adjustViewForReadyToComputeState() {
        binding.computeButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.cancelButton.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        scope = newScope()
    }

    private fun newScope() = CoroutineScope(Job() + Dispatchers.Main)

    override fun onStop() {
        super.onStop()
        scope.cancel()
    }
}
