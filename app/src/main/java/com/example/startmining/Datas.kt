package com.example.startmining

import android.util.Log
import com.example.startmining.ui.pools.Genesis
import com.example.startmining.ui.pools.Horizon
import com.example.startmining.ui.pools.Northpool
import com.example.startmining.ui.pools.Origin
import com.example.startmining.ui.pools.Pulse
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.floor


const val THRESHOLD = 0.005

class Bitcoin {
    companion object {
        val BLOCK2HALVING = 210_000
        val time4block = 10 * 60 // seconds
        var current_block = 0
        var next_halving_block = 0
        var current_halving_nb = 0
        var next_halving_nb = 0
        var date_halving = ""
        var days2halving = 0
        var btc_should_have = 0F
        var get_btc_should_have_thread = Thread { this.GetBtcShouldHave() }
        var get_info_thread = Thread {this.GetInfo()}

        fun GetInfo() {
            /*
            Get information about halving
             */
            val BASE_URL = "https://chain.api.btc.com/v3/block/latest"

            val response = Url2Json(BASE_URL)
            val json = JSONObject(response)
            this.current_block = json.getJSONObject("data").getInt("height")
            val current_halv = floor((this.current_block / this.BLOCK2HALVING).toDouble())
            this.current_halving_nb = current_halv.toInt()
            this.next_halving_nb = this.current_halving_nb + 1
            this.next_halving_block = this.next_halving_nb * this.BLOCK2HALVING

            val days2halving = (this.next_halving_block - this.current_block) * this.time4block / 3600 / 24
            val halving_day = LocalDate.from(LocalDate.now()).plusDays(days2halving.toLong())
            this.days2halving = days2halving
            this.date_halving = "${halving_day.dayOfMonth}/${halving_day.monthValue}/${halving_day.year}"
        }

        fun GetBtcShouldHave() {
            /*
            Number of bitcoin that the user should have if he bought BTC instead of START
             */
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val method = listOf<String>("0xad4bae6f", "0x3776d26d")
            val url = build_eth_url("address" to Datas.eth_wallet, module = "account", action= "txlist")
            val response = Url2Json(url)
            try {
                val json = JSONObject(response)
                val data = json.getJSONArray("result")

                for (i in 0 until data.length()) {
                    val element =
                        data.getJSONObject(i) // Obtenir l'objet JSON à l'indice i dans le tableau JSON
                    if (element.getString("methodId") in method) {
                        val timeStamp = element.getString("timeStamp")
                        val nb_start = element.getString("input")
                            .substring(method[0].length, method[0].length + 64).toInt()
                        Log.e("Custom", timeStamp.toLong().toString())
                        val date = dateFormat.format(Date(timeStamp.toLong() * 1000))
                        val nb_btc = 1000 / GetBtcValue(timeStamp.toLong()).toFloat()
                        Log.e("Test", "${date}  ${nb_start}  ${nb_btc}")
                        this.btc_should_have += nb_btc * nb_start
                    }
                    else{
                        Log.i("methodId", element.getString("methodId"))
                    }
                }
            } catch (cause: Throwable) {
                Log.e("Custom", "Error GetBtcShouldHave: $cause")
            }

        }
    }

}

class Datas {
    companion object {
        var btc_wallet = ""
        var eth_wallet = ""
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
    }
}
