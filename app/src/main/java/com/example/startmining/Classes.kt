package com.example.startmining

import com.example.startmining.ui.pools.Genesis
import com.example.startmining.ui.pools.Northpool
import com.example.startmining.ui.pools.Origin

class Datas {
    companion object {
        var btc_wallet = ""
        var eth_wallet = ""

        var live_rewards: Float = 0F
        var total_payout: Float = 0F
        var earnings: Float = 0F // coin per day
        var next_payout = ""
        var days2payout = ""
        var MainRefreashRunning = false

        fun RefreshStake() {
            this.MainRefreashRunning = true
            Thread { LiveReward() }.start()
            Thread { TotalPayout() }.start()

            val ThreadList: List<Thread> = listOf(
                Thread { Origin.GetStats() },
                Thread { Genesis.GetStats() },
                Thread { Northpool.GetStats() },
            )
            for (t in ThreadList){ t.start() }
            for (t in ThreadList){ t.join() }
            this.MainRefreashRunning = false
        }

        fun RefreshTextValue() {
            while (this.MainRefreashRunning) {
                Thread.sleep(100)
            }
            this.earnings = (Origin.GetMyEarnings() + Genesis.GetMyEarnings() + Northpool.GetMyEarnings())
        }
    }
}
