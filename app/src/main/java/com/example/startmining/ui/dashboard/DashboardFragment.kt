package com.example.startmining.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.startmining.Datas
import com.example.startmining.DateNextPayout
import com.example.startmining.Days2ReachedPayout
import com.example.startmining.RoundBTC
import com.example.startmining.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var liveRewards: TextView
    private lateinit var totalPayout: TextView
    private lateinit var earnings: TextView
    private lateinit var nextPayout: TextView
    private lateinit var reachedPayout: TextView
    private lateinit var Ratio: ProgressBar

    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val mUpdate: Runnable = object : Runnable {
        override fun run() {
            Datas.refresh_thread.join()
            Datas.RefreshTextValue()
            liveRewards.text = RoundBTC(Datas.live_rewards)
            totalPayout.text = RoundBTC(Datas.total_payout)
            earnings.text = RoundBTC(Datas.earnings)
            nextPayout.text = DateNextPayout()
            reachedPayout.text = Days2ReachedPayout()
            Ratio.progress = (Datas.live_rewards / 0.005 * 100).toInt()
            // Planifiez la prochaine exécution de la mise à jour
            //mHandler.postDelayed(this, 100)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialisation des TextViews
        liveRewards = binding.liveRewards
        totalPayout = binding.totalPayout
        earnings = binding.earnings
        nextPayout = binding.nextPayout
        reachedPayout = binding.reachedPayout
        Ratio = binding.Ratio

        // Démarrer la mise à jour périodique
        mHandler.post(mUpdate)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Arrêter la mise à jour lorsque le fragment est détruit
        mHandler.removeCallbacks(mUpdate)
    }
}
