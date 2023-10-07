package com.example.startmining

import org.json.JSONObject
fun main() {

    val contract = "0x31b4c124e8e021402a2b89f8ec1af54f68fd256d"
    val url =
        "https://api.etherscan.io/api?module=account&action=txlist&address=${contract}&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"


    val response = Url2Json(url)
    val json = JSONObject(response)
    var data = json.getJSONArray("result")

    println(data)
}