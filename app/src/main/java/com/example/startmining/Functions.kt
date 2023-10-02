package com.example.startmining

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDate
import kotlin.math.pow


fun Url2Json(url:String): String {

    val url = URL(url)
    val connection = url.openConnection()
    val response = StringBuilder()
    BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->

        var line: String?
        while (inp.readLine().also { line = it } != null) {
            response.append(line)
        }
    }
    return response.toString()
}

fun RoundBTC(btc:Float): String {
    if (btc == 0F){
        return "0"
    }
    return "% .7f".format(btc)
}

fun RoundHashrate(hashrate:Double): String {
    return "% .2f".format(hashrate / 10.0.pow(15))
}

fun LiveReward(): Float {
    try {
        val json = Url2Json("https://cruxpool.com/api/btc/miner/${Datas.btc_wallet}/balance")

        val response = JSONObject(json)
        val data = response.getJSONObject("data")
        val satoshi = data.getString("balance")
        Datas.live_rewards = (satoshi.toFloat() / 10.0.pow(8.0)).toFloat()
    }
    catch (cause: Throwable) {
        Log.e("Custom","Error live_rewards: $cause")
    }
    return Datas.live_rewards
}

fun TotalPayout() {
    try {
        val json = Url2Json("https://cruxpool.com/api/btc/miner/${Datas.btc_wallet}/payments")

        val response = JSONObject(json)
        val data = response.getJSONObject("data")
        val paymentsArray = data.getJSONArray("payments")

        var totalAmount  = 0

        for (i in 0 until paymentsArray.length()) {
            val payment = paymentsArray.getJSONObject(i)
            Log.i(String(), payment.toString())
            val amount = payment.getInt("amount")
            totalAmount += amount
        }
        Datas.total_payout = (totalAmount / 10.0.pow(8.0)).toFloat()
    }
    catch (cause: Throwable) {
        Log.e("Custom","Error TotalPayout: $cause")
    }
}

fun NextPayout(): String {
    val earnings = Datas.earnings
    val rewards = Datas.live_rewards
    if (earnings == 0F){
        return "NaN"
    }
    val payout = 0.005

    val days2wait = (payout - rewards) / earnings
    val payout_day = LocalDate.from(LocalDate.now()).plusDays(days2wait.toLong())
    Datas.next_payout = "${payout_day.dayOfMonth}/${payout_day.monthValue}"
    return Datas.next_payout
}

fun Days2ReachedPayout(): String {
    val earnings = Datas.earnings
    if (earnings == 0F){
        return "NaN"
    }
    val payout = 0.005

    val days2wait = (payout) / earnings
    Datas.days2payout = days2wait.toInt().toString()
    return Datas.days2payout
}