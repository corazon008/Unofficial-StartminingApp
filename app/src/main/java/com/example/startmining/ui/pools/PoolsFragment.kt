package com.example.startmining.ui.pools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.startmining.databinding.FragmentPoolsBinding

class PoolsFragment : Fragment() {

    private var _binding: FragmentPoolsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val poolsViewModel =
            ViewModelProvider(this).get(PoolsViewModel::class.java)

        _binding = FragmentPoolsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPools
        poolsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}