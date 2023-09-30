package com.example.startmining.ui.pools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.startmining.databinding.FragmentPoolsBinding


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

        // Configurer le ViewPager
        val viewPager: ViewPager = binding.viewPager
        val adapter = PoolsPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


class PoolsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OriginFragment()
            1 -> GenesisFragment()
            2 -> NorthpoolFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return 3 // Nombre total de fragments
    }
}
