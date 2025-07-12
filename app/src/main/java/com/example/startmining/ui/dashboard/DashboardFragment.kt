package com.example.startmining.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.startmining.databinding.FragmentDashboardBinding
import kotlin.math.pow

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val mUpdate: Runnable = object : Runnable {
        override fun run() {
           /* Datas.refresh_thread.join()
            Datas.RefreshTextValue()
            Bitcoin.get_btc_should_have_thread.join()
            Bitcoin.get_info_thread.join()

            binding.liveRewards.text = RoundBTC(Datas.live_rewards)
            binding.totalPayout.text = RoundBTC(Datas.total_payout)
            binding.earnings.text = RoundBTC(Datas.earnings)
            binding.nextPayout.text = DateNextPayout()
            binding.reachedPayout.text = Days2ReachedPayout()
            binding.Ratio.progress = (Datas.live_rewards / Constants.PAYMENT_THRESHOLD * 100).toInt()
            binding.btcShouldHave.text = RoundBTC(Bitcoin.btc_should_have)
            binding.btcShouldHaveProgress.progress = (Datas.total_payout / Bitcoin.btc_should_have * 100).toInt()
            binding.dateRoiWithHalving.text = ComputeDateRoi(halving = true)
            binding.dateRoiWithoutHalving.text = ComputeDateRoi(halving = false)*/

            // Planifiez la prochaine exécution de la mise à jour
            //mHandler.postDelayed(this, 100)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Démarrer la mise à jour périodique
        mHandler.post(mUpdate)

        dashboardViewModel.balance.observe(viewLifecycleOwner) {
            binding.liveRewards.text = (it.data.balance / 10.0.pow(8.0)).toString()
        }

        dashboardViewModel.loadBalance("")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacks(mUpdate)
    }
}
