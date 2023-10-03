package com.example.startmining.ui.pools

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.AllStakeFun
import com.example.startmining.MyStakeFun
import com.example.startmining.PoolEarningsFun
import com.example.startmining.RoundBTC
import com.example.startmining.RoundHashrate
import com.example.startmining.databinding.FragmentNorthpoolBinding

class NorthpoolFragment : Fragment() {
    private var _binding: FragmentNorthpoolBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNorthpoolBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Northpool.WaitUntilReady()
        binding.occupancy.text = Northpool.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Northpool.hashrate)
        binding.btcEarnings.text = RoundBTC(Northpool.pool_earnings.toFloat(), 6)
        binding.myStake.text = Northpool.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class Northpool {
    companion object {
        val address = "bc1qyjp7kadrtr8j7gvvs9jej9c790jpmal4cwehle"
        val pool_id = 3
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
            if (this.my_stake > 0) {
                this.all_stake_thread.start()
                this.pool_earnings_thread.start()
                this.all_stake_thread.join()
                this.pool_earnings_thread.join()
            } else {
                this.all_stake_thread.start()
                this.pool_earnings_thread.start()
            }
        }

        fun GetPoolEarnings() {
            this.my_stake_thread.join()
            val value = PoolEarningsFun(this.address)
            this.pool_earnings = value["pool_earnings"]!!
            this.hashrate = value["hashrate"]!!
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
