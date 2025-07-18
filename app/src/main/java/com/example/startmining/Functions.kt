package com.example.startmining

import java.time.LocalDate
import kotlin.math.pow

fun build_eth_url(
    vararg kwargs: Pair<String, String>,
    module: String = "proxy",
    action: String = "eth_call"
): String {
    val CONTRACT_ADDRESS = "0xb4a3c079acbd57668bf5292c13878f9225678381"
    val API_KEY = "ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"
    var url = "https://api.etherscan.io/v2/api?chainid=1&module=${module}&action=${action}&apikey=${API_KEY}&"
    url += kwargs.joinToString("&") { (key, value) -> "$key=$value" }
    if (action == "eth_call") {
        return url + "&to=${CONTRACT_ADDRESS}&tag=latest"
    }
    return url
}

fun RoundBTC(btc: Float, n: Int = 8): String {
    if (btc == 0F) {
        return "0"
    }
    return "% .${n}f".format(btc)
}

fun RoundBTC(btc: Double, n: Int = 8): String {
    if (btc == 0.0) {
        return "0"
    }
    return "% .${n}f".format(btc)
}

fun RoundHashrate(hashrate: Double): String {
    return "% .2f".format(hashrate / 10.0.pow(15))
}

fun DateNextPayout(balance: Double, earnings: Double): String {
    if (earnings == 0.0) {
        return "NaN"
    }

    val days2wait = (Constants.PAYMENT_THRESHOLD - balance) / earnings
    val payout_day = LocalDate.from(LocalDate.now()).plusDays(days2wait.toLong())
    return "${payout_day.dayOfMonth}/${payout_day.monthValue}"
}

fun DaysToReachedPayout(earnings: Double): String {
    if (earnings == 0.0) {
        return "NaN"
    }

    val days2wait = (Constants.PAYMENT_THRESHOLD) / earnings
    return days2wait.toInt().toString()
}

/*fun GetBtcValue(timestamp: Long, crypto: String = "bitcoin"): Double {
    *//*
    Return the price of bitcoin at day day
     *//*
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
}*/

/*
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
}*/
