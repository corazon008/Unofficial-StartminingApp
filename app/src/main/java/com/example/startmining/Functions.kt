package com.example.startmining

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDate
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

fun AllStakeFun(pool_id: Int): Int {
    var nb = 1
    try {
        val url = Datas.base_url + "&data=0x03501951000000000000000000000000000000000000000000000000000000000000000${pool_id}"
        val json: String = Url2Json(url)
        val response = JSONObject(json)
        var data = response.getString("result")
        data = data.substring(2) // Remove the first two characters

        val start = mutableListOf<String>()
        var index = 0
        while (index < data.length) {
            start.add(data.substring(index, minOf(index + 64, data.length)))
            index += 64
        }
        val startNum = start.map { it.toLong(16) }
        nb = startNum.size

    } catch (cause: Throwable) {
        Log.e("Custom", "Error AllStakeFun: $cause")
    }
    return nb - 2
}

fun MyStakeFun(pool_id: Int): Int {
    var nb = 1
    try {
        val url = Datas.base_url + "&data=0xbfafa378000000000000000000000000000000000000000000000000000000000000000${pool_id}000000000000000000000000${Datas.eth_wallet.substring(2)}"
        val json: String = Url2Json(url)
        val response = JSONObject(json)
        var data = response.getString("result")
        data = data.substring(2) // Remove the first two characters

        val start = mutableListOf<String>()
        var index = 0
        while (index < data.length) {
            start.add(data.substring(index, minOf(index + 64, data.length)))
            index += 64
        }
        val startNum = start.map { it.toLong(16) }
        nb = startNum.size

    } catch (cause: Throwable) {
        Log.e("Custom", "Error MyStakeFun: $cause")
    }
    return nb - 2
}

fun PoolEarningsFun(address:String): MutableMap<String, Double> {
    val value = mutableMapOf(
        "pool_earnings" to 0.0,
        "hashrate" to 0.0
    )
    try {
        val json = Url2Json("https://cruxpool.com/api/btc/miner/${address}")
        val response = JSONObject(json)
        val data = response.getJSONObject("data")
        val perMin = data.getDouble("coinPerMins")
        val perDay = perMin * 60 * 24
        value["pool_earnings"] = perDay
        value["hashrate"] = data.getDouble("avgHashrate")
    } catch (cause: Throwable) {
        Log.e("Custom", "Error in PoolEarningsFun: $cause")
    }

    return value

}