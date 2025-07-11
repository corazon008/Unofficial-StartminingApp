package com.example.startmining.ui.pools

import android.util.Log
import com.example.startmining.Datas
import com.example.startmining.Url2Json
import com.example.startmining.build_eth_url
import org.json.JSONObject

fun AllStakeFun(pool_id: Int): Int {
    var nb = 2
    try {
        val url = build_eth_url("data" to "0x03501951000000000000000000000000000000000000000000000000000000000000000${pool_id}")
        val json: String = Url2Json(url)
        val response = JSONObject(json)
        var data = response.getString("result")
        Log.i("Custom", "AllStakeFun for $pool_id : data=${data}")
        data = data.substring(2) // Remove the first two characters

        val start = mutableListOf<String>()
        var index = 0
        while (index < data.length) {
            start.add(data.substring(index, minOf(index + 64, data.length)))
            index += 64
        }
        //val startNum = start.map { it.toLong(16) }
        nb = start.size

    } catch (cause: Throwable) {
        Log.e("Custom", "Error AllStakeFun: $cause")
    }
    return nb - 2
}

fun MyStakeFun(pool_id: Int): Int {
    var nb = 2
    try {
        val url = build_eth_url("data" to "0xbfafa378000000000000000000000000000000000000000000000000000000000000000${pool_id}000000000000000000000000${Datas.eth_wallet.substring(2)}")
        Log.i("Custom", "MyStakeFun for $pool_id : url=${url}")
        val response: String = Url2Json(url)
        val json = JSONObject(response)
        var data = json.getString("result")
        data = data.substring(2) // Remove the first two characters

        val start = mutableListOf<String>()
        var index = 0
        while (index < data.length) {
            start.add(data.substring(index, minOf(index + 64, data.length)))
            index += 64
        }
        //val startNum = start.map { it.toLong(16) }
        nb = start.size

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
        value["hashrate"] = data.getDouble("realtimeHashrate")
    } catch (cause: Throwable) {
        Log.e("Custom", "Error in PoolEarningsFun: $cause")
    }
    return value
}