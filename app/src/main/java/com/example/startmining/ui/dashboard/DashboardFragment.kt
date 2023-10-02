package com.example.startmining.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.startmining.Datas
import com.example.startmining.Days2ReachedPayout
import com.example.startmining.NextPayout
import com.example.startmining.RoundBTC
import com.example.startmining.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {
    private var mHandler: Handler? = null
    private var liveRewards: TextView? = null
    private var totalPayout: TextView? = null
    private var earnings: TextView? = null
    private var nextPayout: TextView? = null
    private var reachedPayout: TextView? = null

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Datas.refresh_thread.join()
        liveRewards = binding.liveRewards
        totalPayout = binding.totalPayout
        earnings = binding.earnings
        nextPayout = binding.nextPayout
        reachedPayout = binding.reachedPayout

        mHandler = Handler()
        mHandler!!.post(mUpdate)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private val mUpdate: Runnable = object : Runnable {
        override fun run() {
            Datas.refresh_thread.join()
            Datas.RefreshTextValue()
            liveRewards!!.text = RoundBTC(Datas.live_rewards)
            totalPayout!!.text = RoundBTC(Datas.total_payout)
            earnings!!.text = RoundBTC(Datas.earnings)
            nextPayout!!.text = NextPayout()
            reachedPayout!!.text = Days2ReachedPayout()
        }
    }
}

