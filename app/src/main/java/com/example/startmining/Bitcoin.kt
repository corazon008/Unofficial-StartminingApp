package com.example.startmining

import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.floor

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
        var get_info_thread = Thread { this.GetInfo() }

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

            val days2halving =
                (this.next_halving_block - this.current_block) * this.time4block / 3600 / 24
            val halving_day = LocalDate.from(LocalDate.now()).plusDays(days2halving.toLong())
            this.days2halving = days2halving
            this.date_halving =
                "${halving_day.dayOfMonth}/${halving_day.monthValue}/${halving_day.year}"
        }

        fun GetBtcShouldHave() {
            /*
            Number of bitcoin that the user should have if he bought BTC instead of START
             */
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val method = listOf<String>("0xad4bae6f", "0x3776d26d")
            val url =
                build_eth_url("address" to Datas.eth_wallet, module = "account", action = "txlist")
            val response = Url2Json(url)
            try {
                val json = JSONObject(response)
                val data = json.getJSONArray("result")
                val length = data.length()

                for (i in 0 until length) {
                    val element =
                        data.getJSONObject(i) // Obtenir l'objet JSON à l'indice i dans le tableau JSON
                    if (element.getString("methodId") in method) {
                        val timeStamp = element.getString("timeStamp").toLong() * 1000
                        val nb_start = element.getString("input")
                            .substring(method[0].length, method[0].length + 64).toInt()
                        val date = dateFormat.format(Date(timeStamp))
                        val nb_btc = 1000 / GetBtcValue(timeStamp).toFloat()
                        Log.e("Test", "${date}  ${nb_start}  ${nb_btc}")
                        this.btc_should_have += nb_btc * nb_start
                    } else {
                        //Log.i("methodId", element.getString("methodId"))
                    }
                }
            } catch (cause: Throwable) {
                Log.e("Custom", "Error GetBtcShouldHave: $cause")
            }

        }
    }

}