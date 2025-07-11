package com.example.startmining.ui.pools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.startmining.databinding.FragmentPoolsBinding
import com.example.startmining.ui.pools.genesis.GenesisFragment
import com.example.startmining.ui.pools.horizon.HorizonFragment
import com.example.startmining.ui.pools.northPool.NorthPoolFragment
import com.example.startmining.ui.pools.origin.OriginFragment
import com.example.startmining.ui.pools.pulse.PulseFragment

class PoolsFragment : Fragment() {

    private var _binding: FragmentPoolsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoolsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurer le ViewPager2
        val viewPager: ViewPager2 = binding.viewPager
        val adapter = PoolsPagerAdapter(this)
        viewPager.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class PoolsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 5 // Nombre total de fragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OriginFragment()
            1 -> GenesisFragment()
            2 -> NorthPoolFragment()
            3 -> PulseFragment()
            4 -> HorizonFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}