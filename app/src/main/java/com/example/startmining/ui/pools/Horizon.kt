package com.example.startmining.ui.pools

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.databinding.FragmentHorizonBinding

class HorizonFragment : Fragment() {
    private var _binding: FragmentHorizonBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHorizonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Horizon.WaitUntilReady()
        binding.occupancy.text = Horizon.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Horizon.hashrate)
        binding.btcEarnings.text = RoundBTC((Horizon.pool_earnings / Horizon.all_stake).toFloat(), 6)
        binding.myStake.text = Horizon.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class Horizon {
    companion object {
        val address = "bc1qyjp7kadrtr8j7gvvs9jej9c790jpmal4cwehle"
        val pool_id = 5
        var my_stake = 0
        var all_stake = 1
        var my_earnings: Double = 0.0
        var pool_earnings = 0.0
        var hashrate = 0.0
        val my_stake_thread = Thread { this.GetMyStake() }
        val all_stake_thread = Thread { this.GetAllStake() }
        val pool_earnings_thread = Thread { this.GetPoolEarnings() }

        fun WaitUntilReady() {
            this.my_stake_thread.join()
            this.all_stake_thread.join()
            this.pool_earnings_thread.join()
        }

        fun GetStats() {
            this.my_stake_thread.start()
            this.my_stake_thread.join()

            this.all_stake_thread.start()
            this.pool_earnings_thread.start()
            if (this.my_stake > 0) {
                this.WaitUntilReady()
            }
        }

        fun GetPoolEarnings() {
            this.my_stake_thread.join()
            while (this.pool_earnings <= 0 || this.hashrate <= 0) {
                val value = PoolEarningsFun(this.address)
                this.pool_earnings = value["pool_earnings"]!!
                this.hashrate = value["hashrate"]!!
            }
        }

        fun GetMyEarnings(): Float {
            try {
                this.my_earnings = this.pool_earnings / this.all_stake * this.my_stake
            } catch (cause: Throwable) {
                Log.e("Custom", "Error in Origin.GetMyEarnings: $cause")
            }
            return this.my_earnings.toFloat()
        }

        fun GetAllStake() {
            this.my_stake_thread.join()
            this.all_stake = AllStakeFun(this.pool_id)

        }

        fun GetMyStake() {
            this.my_stake = MyStakeFun(this.pool_id)
        }

    }
}
