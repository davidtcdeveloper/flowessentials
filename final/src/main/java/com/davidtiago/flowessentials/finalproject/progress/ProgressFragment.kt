package com.davidtiago.flowessentials.finalproject.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.davidtiago.flowessentials.finalproject.R
import com.davidtiago.flowessentials.finalproject.databinding.FragmentProgressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgressFragment : Fragment() {
    private val args: ProgressFragmentArgs by navArgs()
    private val viewModel: ProgressViewModel by viewModels()

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        binding.cancelButton.setOnClickListener { requireActivity().onBackPressed() }
        viewModel.computingProgress.observe(viewLifecycleOwner) { progress ->
            when (progress) {
                is ComputationProgress.Computing -> {
                    updateProgress(progress)
                }
                is ComputationProgress.Completed -> {
                    navigateToCompleted(progress)
                }
            }
        }
        viewModel.computeDivisors(args.input)
        return binding.root
    }

    private fun navigateToCompleted(progress: ComputationProgress.Completed) {
        findNavController().navigate(
            ProgressFragmentDirections.actionProgressFragmentToResultFragment(
                number = progress.computedNumber,
                divisors = progress.divisors,
            )
        )
    }

    private fun updateProgress(progress: ComputationProgress.Computing) {
        with(binding) {
            progressBar.max = progress.maxProgress
            progressBar.progress = progress.currentProgress
            textView.text = getString(R.string.computing_if_d_is_prime, progress.number)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
