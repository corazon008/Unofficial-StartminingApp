package com.example.startmining.ui.pools.genesis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.databinding.FragmentGenesisBinding

class GenesisFragment : Fragment() {
    private var _binding: FragmentGenesisBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenesisBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Genesis.WaitUntilReady()
        binding.occupancy.text = Genesis.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Genesis.hashrate)
        binding.btcEarnings.text = RoundBTC((Genesis.pool_earnings / Genesis.all_stake).toFloat(), 6)
        binding.myStake.text = Genesis.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}