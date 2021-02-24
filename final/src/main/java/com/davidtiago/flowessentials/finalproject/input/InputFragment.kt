package com.davidtiago.flowessentials.finalproject.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.davidtiago.flowessentials.finalproject.databinding.FragmentInputBinding

class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        binding.computeButton.setOnClickListener {
            val number = binding.inputNumberEditText.text.toString().toLong()
            findNavController()
                .navigate(
                    InputFragmentDirections.actionInputFragmentToProgressFragment(number)
                )
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
