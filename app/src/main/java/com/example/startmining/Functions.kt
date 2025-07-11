package com.example.startmining

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import javax.net.ssl.HttpsURLConnection
import kotlin.math.pow

fun build_eth_url(
    vararg kwargs: Pair<String, String>,
    module: String = "proxy",
    action: String = "eth_call"
): String {
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
    val delay: Long = 300
    var attempt = 0
    val maxAttempts = 5

    while (attempt < maxAttempts) {
        try {
            val connection = URL(url).openConnection() as HttpsURLConnection
            val response = StringBuilder()

            if (connection.responseCode != 200) {
                Thread.sleep(delay)
                attempt++
                continue
            }

            BufferedReader(InputStreamReader(connection.inputStream)).use { inp ->
                var line: String?
                while (inp.readLine().also { line = it } != null) {
                    response.append(line)
                }
            }

            val text_response: String = response.toString()

            if ("imit" in text_response) { // Pour "limit" et "Limit"
                Thread.sleep(delay)
                attempt++
                continue
            }

            return text_response
        } catch (e: FileNotFoundException) {
            Thread.sleep(delay)
            attempt++
            continue
        }
    }

    throw Exception("Failed to retrieve a valid response after $maxAttempts attempts")
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

fun GetBtcValue(timestamp: Long, crypto: String = "bitcoin"): Double {
    /*
    Return the price of bitcoin at day day
     */
    val url =
        "https://data.block.cc/api/v3/price/history?slug=${crypto}&api_key=X0QVBMSGHYQ2F7GGQRDJHRYXUYH8QGAOEWAMU9VA&start=${timestamp}&end=${timestamp + 600_000}"
    val response = Url2Json(url)
    try {
        val json = JSONObject(response)
        val error = json.getString("m")
        return GetBtcValue(timestamp, crypto)
    } catch (cause: Throwable) {
        Log.e("Custom", "Error GetBtcValue1: $cause")
        Log.e("Custom", "Error GetBtcValue1 response: $response")
    }
    try {
        val json = JSONArray(response)
        return json.getJSONObject(0).getDouble("u")
    } catch (cause: Throwable) {
        Log.e("Custom", "Error GetBtcValue2: $cause")
        Log.e("Custom", "Error GetBtcValue2 response: $response")
        return 0.0
    }
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