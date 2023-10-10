package com.example.startmining.ui.pools

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.databinding.FragmentOriginBinding

class OriginFragment : Fragment() {
    private var _binding: FragmentOriginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOriginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Origin.WaitUntilReady()
        binding.occupancy.text = Origin.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Origin.hashrate)
        binding.btcEarnings.text = RoundBTC((Origin.pool_earnings / Origin.all_stake).toFloat(), 6)
        binding.myStake.text = Origin.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class Origin {
    companion object {
        val address = "bc1qdzcgvennnjzv4jry38s0krjtl3x9n374302c75"
        val pool_id = 1
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
