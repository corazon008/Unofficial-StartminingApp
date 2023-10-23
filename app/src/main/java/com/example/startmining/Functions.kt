package com.example.startmining

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDate
import kotlin.math.pow

fun build_eth_url(vararg kwargs: Pair<String, String>,module: String = "proxy",action: String = "eth_call"): String {
    val CONTRACT_ADDRESS = "0xb4a3c079acbd57668bf5292c13878f9225678381"
    val API_KEY = "ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"
    var url = "https://api.etherscan.io/api?module=${module}&action=${action}&apikey=${API_KEY}&"
    url += kwargs.joinToString("&") { (key, value) -> "$key=$value" }
    if (action == "eth_call") {
        return url + "&to=${CONTRACT_ADDRESS}&tag=latest"
    }
    return url
}

fun Url2Json(url: String): String {
    val connection = URL(url).openConnection()
    val response = StringBuilder()

    BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
        var line: String?
        while (inp.readLine().also { line = it } != null) {
            response.append(line)
        }
    }

    var text_response: String = response.toString()
    if ("limit reached" in response.toString()) {
        Thread.sleep(100)
        text_response = Url2Json(url)
    }
    return text_response
}

fun RoundBTC(btc: Float, n: Int = 8): String {
    if (btc == 0F) {
        return "0"
    }
    return "% .${n}f".format(btc)
}

fun RoundHashrate(hashrate: Double): String {
    return "% .2f".format(hashrate / 10.0.pow(15))
}

fun LiveReward(): Float {
    try {
        val json = Url2Json("https://cruxpool.com/api/btc/miner/${Datas.btc_wallet}/balance")

        val response = JSONObject(json)
        val data = response.getJSONObject("data")
        val satoshi = data.getString("balance")
        Datas.live_rewards = (satoshi.toFloat() / 10.0.pow(8.0)).toFloat()
    } catch (cause: Throwable) {
        Log.e("Custom", "Error live_rewards: $cause")
    }
    return Datas.live_rewards
}

fun TotalPayout() {
    try {
        val json = Url2Json("https://cruxpool.com/api/btc/miner/${Datas.btc_wallet}/payments")

        val response = JSONObject(json)
        val data = response.getJSONObject("data")
        val paymentsArray = data.getJSONArray("payments")

        var totalAmount = 0

        for (i in 0 until paymentsArray.length()) {
            val payment = paymentsArray.getJSONObject(i)
            Log.i(String(), payment.toString())
            val amount = payment.getInt("amount")
            totalAmount += amount
        }
        Datas.total_payout = (totalAmount / 10.0.pow(8.0)).toFloat()
    } catch (cause: Throwable) {
        Log.e("Custom", "Error TotalPayout: $cause")
    }
}

fun DateNextPayout(): String {
    val earnings = Datas.earnings
    val rewards = Datas.live_rewards
    if (earnings == 0F) {
        return "NaN"
    }

    val days2wait = (THRESHOLD - rewards) / earnings
    val payout_day = LocalDate.from(LocalDate.now()).plusDays(days2wait.toLong())
    Datas.days2wait = days2wait
    Datas.date_next_payout = "${payout_day.dayOfMonth}/${payout_day.monthValue}"
    return Datas.date_next_payout
}

fun Days2ReachedPayout(): String {
    val earnings = Datas.earnings
    if (earnings == 0F) {
        return "NaN"
    }

    val days2wait = (THRESHOLD) / earnings
    Datas.days4payout = days2wait.toInt().toString()
    return Datas.days4payout
}

fun GetBtcValue(day: String, crypto: String = "bitcoin"): String {
    val url =
        "https://api.coingecko.com/api/v3/coins/${crypto}/history?date=${day}&localization=false"
    val response = Url2Json(url)
    val json = JSONObject(response)
    return json.getJSONObject("market_data").getJSONObject("current_price").getString("usd")
}

fun ComputeDateRoi(halving: Boolean = true): String {
    val earnings = Datas.earnings
    val btc_should_have = Bitcoin.btc_should_have
    val total_payout = Datas.total_payout
    val live_rewards = Datas.live_rewards

    val days_until_halving = Bitcoin.days2halving
    val btc2win = btc_should_have - total_payout - live_rewards
    val days2wait = btc2win / earnings
    val ROI_date: LocalDate
    val now = LocalDate.from(LocalDate.now())
    ROI_date = if (halving) {
        if (days2wait <= days_until_halving) {
            now.plusDays(days2wait.toLong())
        } else {
            now.plusDays((days_until_halving + (btc2win - earnings * days_until_halving) / (earnings / 2)).toLong())
        }
    } else {
        now.plusDays(days2wait.toLong())
    }
    return "${ROI_date.dayOfMonth}/${ROI_date.monthValue}/${ROI_date.year}"
}