package com.example.startmining.ui.pools.pulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.databinding.FragmentPulseBinding

class PulseFragment : Fragment() {
    private var _binding: FragmentPulseBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPulseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Pulse.WaitUntilReady()
        binding.occupancy.text = Pulse.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Pulse.hashrate)
        binding.btcEarnings.text = RoundBTC((Pulse.pool_earnings / Pulse.all_stake).toFloat(), 6)
        binding.myStake.text = Pulse.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}