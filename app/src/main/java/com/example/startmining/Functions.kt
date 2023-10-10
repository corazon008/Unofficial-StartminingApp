package com.example.startmining

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.TimeZone
import kotlin.math.floor
import kotlin.math.pow


fun Url2Json(url:String): String {
    val connection = URL(url).openConnection()
    val response = StringBuilder()

    BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->

        var line: String?
        while (inp.readLine().also { line = it } != null) {
            response.append(line)
        }
    }

    var text_response: String = response.toString()
    if("limit reached" in response.toString()){
        Thread.sleep(100)
        text_response = Url2Json(url)
    }
    return text_response
}

fun RoundBTC(btc:Float, n: Int = 8): String {
    if (btc == 0F){
        return "0"
    }
    return "% .${n}f".format(btc)
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

fun DateNextPayout(): String {
    val earnings = Datas.earnings
    val rewards = Datas.live_rewards
    if (earnings == 0F){
        return "NaN"
    }
    val payout = 0.005

    val days2wait = (payout - rewards) / earnings
    val payout_day = LocalDate.from(LocalDate.now()).plusDays(days2wait.toLong())
    Datas.days2wait = days2wait
    Datas.date_next_payout = "${payout_day.dayOfMonth}/${payout_day.monthValue}"
    return Datas.date_next_payout
}

fun Days2ReachedPayout(): String {
    val earnings = Datas.earnings
    if (earnings == 0F){
        return "NaN"
    }
    val payout = 0.005

    val days2wait = (payout) / earnings
    Datas.days4payout = days2wait.toInt().toString()
    return Datas.days4payout
}

fun GetCurrentHalving(): Double {
    val BASE_URL = "https://chain.api.btc.com/v3/block/latest"

    val response = Url2Json(BASE_URL)
    val json = JSONObject(response)
    val block_nb = json.getJSONObject("data").getInt("height")
    val current_halv = floor((block_nb / BLOCK2HALVING).toDouble())
    return current_halv +1
}



fun GetBtcShouldHave() {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val method = listOf<String>("0xad4bae6f", "0x3776d26d")
    val url =
        "https://api.etherscan.io/api?module=account&action=txlist&address=${"0x7372C3A677ac01F389A87B4Fc8614C0d241CC971"}&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"
    val response = Url2Json(url)
    val json = JSONObject(response)
    val data = json.getJSONArray("result")

    for (i in 0 until data.length()) {
        val element = data.getJSONObject(i) // Obtenir l'objet JSON à l'indice i dans le tableau JSON
        if (element.getString("methodId") in method) {
            val timeStamp = element.getString("timeStamp")
            val nb_start = element.getString("input").substring(method[0].length, method[0].length + 64).toInt()
            val date =dateFormat.format(Date(timeStamp.toLong() * 1000))
            val nb_btc = 1000 / GetBtcValue(date).toFloat()
            Log.e("Test", "${date}  ${nb_start}     ${nb_btc}")
            Datas.btc_should_have += nb_btc * nb_start
        }
    }
}

fun GetBtcValue(day: String, crypto: String = "bitcoin"): String {
    val url =
        "https://api.coingecko.com/api/v3/coins/${crypto}/history?date=${day}&localization=false"
    val response = Url2Json(url)
    val json = JSONObject(response)
    return json.getJSONObject("market_data").getJSONObject("current_price").getString("usd")
}