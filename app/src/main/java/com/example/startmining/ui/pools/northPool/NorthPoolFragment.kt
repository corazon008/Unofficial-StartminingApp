package com.example.startmining.ui.pools.northPool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.databinding.FragmentNorthpoolBinding

class NorthPoolFragment : Fragment() {
    private var _binding: FragmentNorthpoolBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNorthpoolBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Northpool.WaitUntilReady()
        binding.occupancy.text = Northpool.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Northpool.hashrate)
        binding.btcEarnings.text = RoundBTC((Northpool.pool_earnings / Northpool.all_stake).toFloat(), 6)
        binding.myStake.text = Northpool.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}