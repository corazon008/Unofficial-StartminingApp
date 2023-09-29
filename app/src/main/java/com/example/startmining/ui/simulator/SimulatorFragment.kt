package com.example.startmining.ui.simulator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.startmining.databinding.FragmentSimulatorBinding

class SimulatorFragment: Fragment() {

    private var _binding: FragmentSimulatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val simulatorViewModel =
            ViewModelProvider(this).get(SimulatorViewModel::class.java)

        _binding = FragmentSimulatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSimulator
        simulatorViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}