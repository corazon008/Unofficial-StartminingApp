package com.example.startmining

import com.example.startmining.ui.pools.Genesis
import com.example.startmining.ui.pools.Northpool
import com.example.startmining.ui.pools.Origin


const val CONTRACT_ADDRESS: String = "0xb4a3c079acbd57668bf5292c13878f9225678381"
const val BASE_URL:String = "https://api.etherscan.io/api?module=proxy&action=eth_call&to=${CONTRACT_ADDRESS}&tag=latest&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"

class Datas {

    companion object {
        var btc_wallet = ""
        var eth_wallet = ""
        var live_rewards: Float = 0F

        var total_payout: Float = 0F
        var btc_would_have = 0F
        var earnings: Float = 0F // coin per day
        var date_next_payout = ""
        var days2wait = 0.0
        var days4payout = ""
        var refresh_thread = Thread { this.RefreshStake() }
        var get_btc_would_have_thread = Thread { GetBtcWouldHave() }

        fun RefreshStake() {
            Thread { LiveReward() }.start()
            Thread { TotalPayout() }.start()

            val ThreadList: List<Thread> = listOf(
                Thread { Origin.GetStats() },
                Thread { Genesis.GetStats() },
                Thread { Northpool.GetStats() },
            )
            for (t in ThreadList){ t.start() }
            for (t in ThreadList){ t.join() }
        }

        fun RefreshTextValue() {
            this.refresh_thread.join()
            this.earnings = (Origin.GetMyEarnings() + Genesis.GetMyEarnings() + Northpool.GetMyEarnings())
        }
    }
}
