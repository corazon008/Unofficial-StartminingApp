package com.example.startmining.ui.pools.pulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.SessionManager
import com.example.startmining.databinding.FragmentPulseBinding
import com.example.startmining.network.pools.PoolsService

class PulseFragment : Fragment() {
    private var _binding: FragmentPulseBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPulseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val poolId = PoolsService.pulse_pool_id

        SessionManager.poolListInfo.observe(viewLifecycleOwner) {
            binding.occupancy.text = it[poolId - 1].nbStakedNft.toString()
            binding.hashrate.text = RoundHashrate(it[poolId - 1].hashrate)
            binding.btcEarnings.text =
                RoundBTC((it[poolId - 1].poolEarnings / it[poolId - 1].nbStakedNft).toFloat(), 6)
            binding.myStake.text = it[poolId - 1].nbStakedNftUser.toString()
        }

        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}