package com.davidtiago.flowessentials.finalproject

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.davidtiago.flowessentials.finalproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: ComputingViewModel by viewModels()

    @Inject
    lateinit var primeNumberComputer: PrimeNumberComputer

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.computingProgress.observe(this) { progress ->
            when (progress) {
                is ComputationProgress.Completed -> handleCompleted(progress)
                is ComputationProgress.Computing -> handleComputing(progress)
            }
        }

        adjustViewForReadyToComputeState()
        binding.computeButton.setOnClickListener {
            val number = binding.editTextNumber.text.toString().toLong()
            viewModel.computeDivisors(number)
        }
        binding.cancelButton.setOnClickListener {
            viewModel.cancelComputing()
            //TODO remove this state change from here
            binding.textView.text = "Computation cancelled"
            adjustViewForReadyToComputeState()
        }
    }

    private fun handleComputing(progress: ComputationProgress.Computing) {
        lifecycleScope.launch {
            with(binding) {
                binding.textView.text = ""
                progressBar.visibility = View.VISIBLE
                computeButton.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
                progressBar.max = progress.maxProgress
                progressBar.progress = progress.currentProgress
            }
        }
    }

    private fun handleCompleted(
        progress: ComputationProgress.Completed
    ) {
        if (progress.isPrime) {
            binding.textView.text = "${progress.computedNumber} \n is a prime number 👍"
        } else {
            binding.textView.text =
                "${progress.computedNumber} \n is NOT a prime number 👎 \n can be divided by ${progress.divisors} other numbers"
        }
        adjustViewForReadyToComputeState()
    }

    private fun adjustViewForReadyToComputeState() {
        binding.computeButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.cancelButton.visibility = View.GONE
    }
}
