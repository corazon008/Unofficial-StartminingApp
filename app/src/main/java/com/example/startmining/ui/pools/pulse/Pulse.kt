package com.example.startmining.ui.pools.pulse

import android.util.Log
import com.example.startmining.ui.pools.AllStakeFun
import com.example.startmining.ui.pools.MyStakeFun
import com.example.startmining.ui.pools.PoolEarningsFun

class Pulse {
    companion object {
        val address = "bc1qaj5lgj8zmhap0vquttpud3dhat43y8azplgzqf"
        val pool_id = 4
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
                Log.e("Custom", "Error in Pulse.GetMyEarnings: $cause")
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
