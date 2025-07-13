package com.example.startmining.ui.pools.origin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.SessionManager
import com.example.startmining.databinding.FragmentOriginBinding
import com.example.startmining.network.pools.PoolsService

class OriginFragment : Fragment() {
    private var _binding: FragmentOriginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOriginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val poolId = PoolsService.origin_pool_id

        SessionManager.poolListInfo.observe(viewLifecycleOwner) {
            binding.occupancy.text = it[poolId-1].nbStakedNft.toString()
            binding.hashrate.text = RoundHashrate(it[poolId-1].hashrate)
            binding.btcEarnings.text = RoundBTC((it[poolId-1].poolEarnings / it[poolId-1].nbStakedNft).toFloat(), 6)
            binding.myStake.text = it[poolId-1].nbStakedNftUser.toString()
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