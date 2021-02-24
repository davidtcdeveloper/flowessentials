package com.davidtiago.flowessentials.finalproject.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.davidtiago.flowessentials.finalproject.ComputationProgress
import com.davidtiago.flowessentials.finalproject.databinding.FragmentProgressBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        viewModel.computingProgress.observe(viewLifecycleOwner) { progress ->
            when (progress) {
                is ComputationProgress.Computing -> { //TODO
                }
                is ComputationProgress.Completed -> {
                    findNavController().navigate(
                        ProgressFragmentDirections.actionProgressFragmentToResultFragment()
                    )
                }
            }
        }
        Timber.d("Args input value %d", args.input)
        viewModel.computeDivisors(args.input)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
