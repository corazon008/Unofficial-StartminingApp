package com.example.startmining.ui.pools.horizon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.databinding.FragmentHorizonBinding

class HorizonFragment : Fragment() {
    private var _binding: FragmentHorizonBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHorizonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Horizon.WaitUntilReady()
        binding.occupancy.text = Horizon.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Horizon.hashrate)
        binding.btcEarnings.text = RoundBTC((Horizon.pool_earnings / Horizon.all_stake).toFloat(), 6)
        binding.myStake.text = Horizon.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}