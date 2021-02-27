package com.davidtiago.flowessentials.finalproject.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.davidtiago.flowessentials.finalproject.R
import com.davidtiago.flowessentials.finalproject.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        binding.textView.text =
            if (args.divisors != 0.toLong()) {
                getString(
                    R.string.is_not_prime_number,
                    args.number,
                    args.divisors
                )
            } else {
                getString(R.string.is_prime_number, args.number)
            }
        binding.checkAnotherNumberButton.setOnClickListener { requireActivity().onBackPressed() }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
