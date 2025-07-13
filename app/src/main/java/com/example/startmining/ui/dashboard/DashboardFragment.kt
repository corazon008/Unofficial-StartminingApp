package com.example.startmining.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.Constants
import com.example.startmining.Datas
import com.example.startmining.DateNextPayout
import com.example.startmining.DaysToReachedPayout
import com.example.startmining.RoundBTC
import com.example.startmining.SessionManager
import com.example.startmining.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        SessionManager.balance.observe(viewLifecycleOwner) {
            val balance: Double = it
            binding.balance.text = RoundBTC(balance).toString()
            binding.Ratio.progress = (balance / Constants.PAYMENT_THRESHOLD * 100).toInt()
            binding.nextPayout.text = DateNextPayout(balance, SessionManager.userEarnings.value ?: 0.0)
        }

        SessionManager.payments.observe(viewLifecycleOwner) {
            binding.totalPayout.text = RoundBTC(it).toString()
        }

        SessionManager.userEarnings.observe(viewLifecycleOwner) {
            binding.earnings.text = RoundBTC(it).toString()
            binding.nextPayout.text = DateNextPayout(SessionManager.balance.value ?: 0.0, it)
            binding.reachedPayout.text = DaysToReachedPayout(it)
        }

        /*
        binding.btcShouldHave.text = RoundBTC(Bitcoin.btc_should_have)
        binding.btcShouldHaveProgress.progress = (Datas.total_payout / Bitcoin.btc_should_have * 100).toInt()
        binding.dateRoiWithHalving.text = ComputeDateRoi(halving = true)
        binding.dateRoiWithoutHalving.text = ComputeDateRoi(halving = false)*/

        SessionManager.updateBalance(Datas.btc_wallet)
        SessionManager.updatePayments(Datas.btc_wallet)
        SessionManager.updatePoolsInfo(Datas.eth_wallet)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
