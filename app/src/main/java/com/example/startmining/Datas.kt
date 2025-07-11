package com.example.startmining

import android.content.Context
import com.example.startmining.ui.pools.genesis.Genesis
import com.example.startmining.ui.pools.horizon.Horizon
import com.example.startmining.ui.pools.northPool.Northpool
import com.example.startmining.ui.pools.origin.Origin
import com.example.startmining.ui.pools.pulse.Pulse

class Datas {
    companion object {
        var btc_wallet: String = ""
            get() {
                return field
            }
            private set(value) {
                field = value
            }

        var eth_wallet: String = ""
            get() {
                return field
            }
            private set(value) {
                field = value
            }

        var live_rewards: Float = 0F

        var total_payout: Float = 0F
        var earnings: Float = 0F // coin per day
        var date_next_payout = ""
        var days2wait = 0.0
        var days4payout = ""
        var refresh_thread = Thread { this.RefreshStake() }

        fun RefreshStake() {
            Thread { LiveReward() }.start()
            Thread { TotalPayout() }.start()

            val ThreadList: List<Thread> = listOf(
                Thread { Origin.GetStats() },
                Thread { Genesis.GetStats() },
                Thread { Northpool.GetStats() },
                Thread { Pulse.GetStats() },
                Thread { Horizon.GetStats() }

            )
            for (t in ThreadList) {
                t.start()
            }
            for (t in ThreadList) {
                t.join()
            }
        }

        fun RefreshTextValue() {
            this.refresh_thread.join()
            this.earnings =
                (Origin.GetMyEarnings() + Genesis.GetMyEarnings() + Northpool.GetMyEarnings() + Pulse.GetMyEarnings() + Horizon.GetMyEarnings())
        }

        fun LoadWalletsAddress(application: Context?) {
            val sharedPref =
                application?.getSharedPreferences(application.getString(R.string.file_name), Context.MODE_PRIVATE)
            this.btc_wallet = sharedPref!!.getString(application.getString(R.string.btc_address), "0x0000000000000000000000000000000000000000").toString()
            this.eth_wallet = sharedPref.getString(application.getString(R.string.eth_address), "0x0000000000000000000000000000000000000000").toString()
        }

        fun SaveWalletsAddress(application: Context?, btc_address: String, eth_address: String) {
            val sharedPref = application?.getSharedPreferences(application.getString(R.string.file_name), Context.MODE_PRIVATE)
            if (sharedPref != null) {
                with (sharedPref.edit()) {
                    putString(application.getString(R.string.btc_address), btc_address.toString())
                    putString(application.getString(R.string.eth_address), eth_address.toString())
                    commit()
                }
            }
        }

    }
}
