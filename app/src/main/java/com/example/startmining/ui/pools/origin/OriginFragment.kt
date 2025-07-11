package com.example.startmining.ui.pools.origin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.databinding.FragmentOriginBinding

class OriginFragment : Fragment() {
    private var _binding: FragmentOriginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOriginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Origin.WaitUntilReady()
        binding.occupancy.text = Origin.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Origin.hashrate)
        binding.btcEarnings.text = RoundBTC((Origin.pool_earnings / Origin.all_stake).toFloat(), 6)
        binding.myStake.text = Origin.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}