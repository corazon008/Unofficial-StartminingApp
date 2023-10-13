package com.example.startmining.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.Bitcoin
import com.example.startmining.ComputeDateRoi
import com.example.startmining.Datas
import com.example.startmining.DateNextPayout
import com.example.startmining.Days2ReachedPayout
import com.example.startmining.RoundBTC
import com.example.startmining.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val mUpdate: Runnable = object : Runnable {
        override fun run() {
            Datas.refresh_thread.join()
            Datas.RefreshTextValue()
            Bitcoin.get_btc_should_have_thread.join()
            Bitcoin.get_info_thread.join()

            binding.liveRewards.text = RoundBTC(Datas.live_rewards)
            binding.totalPayout.text = RoundBTC(Datas.total_payout)
            binding.earnings.text = RoundBTC(Datas.earnings)
            binding.nextPayout.text = DateNextPayout()
            binding.reachedPayout.text = Days2ReachedPayout()
            binding.Ratio.progress = (Datas.live_rewards / 0.005 * 100).toInt()
            binding.btcShouldHave.text = RoundBTC(Bitcoin.btc_should_have)
            binding.btcShouldHaveProgress.progress = (Datas.total_payout / Bitcoin.btc_should_have * 100).toInt()
            binding.dateRoi.text = ComputeDateRoi()

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

        // Démarrer la mise à jour périodique
        mHandler.post(mUpdate)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacks(mUpdate)
    }
}
