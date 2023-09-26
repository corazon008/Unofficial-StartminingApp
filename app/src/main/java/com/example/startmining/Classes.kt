package com.example.startmining

import android.util.Log
import org.json.JSONObject


class Origin {
    companion object {
        val address = "bc1qdzcgvennnjzv4jry38s0krjtl3x9n374302c75"
        val pool_id = 1
        var my_stake = 0
        var all_stake = 1
        var earnings: Double = 0.0
        var pool_earnings = 0.0
        var my_stake_running = false

        fun GetPoolEarnings(){
            while (this.my_stake_running) { Thread.sleep(100) }
            if (this.my_stake > 0) {
                try {
                    val json = Url2Json("https://cruxpool.com/api/btc/miner/${this.address}")
                    val response = JSONObject(json)
                    val data = response.getJSONObject("data")
                    val perMin = data.getDouble("coinPerMins")
                    val perDay = perMin * 60 * 24
                    this.pool_earnings = perDay
                } catch (cause: Throwable) {
                    Log.e("Custom", "Error in Origin.GetPoolEarnings: $cause")
                }
            }
        }

        fun GetMyEarnings(): Float {
            try {
                this.earnings = this.pool_earnings / this.all_stake * this.my_stake
            }
            catch (cause: Throwable) {
                Log.e("Custom","Error in Origin.GetMyEarnings: $cause")
            }
            return this.earnings.toFloat()
        }

        fun GetAllStake() {
            while (this.my_stake_running) { Thread.sleep(100) }
            if (this.my_stake > 0) {
                try {
                    val url =
                        "https://api.etherscan.io/api?module=proxy&action=eth_call&to=0xb4a3c079acbd57668bf5292c13878f9225678381&data=0x03501951000000000000000000000000000000000000000000000000000000000000000${this.pool_id}&tag=latest&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"
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
                    val nb = startNum.size
                    this.all_stake = nb - 2
                } catch (cause: Throwable) {
                    Log.e("Custom", "Error Origin.GetAllStake: $cause")
                }
            }
        }

        fun GetMyStake() {
            this.my_stake_running = true
            try {
                val url =
                    "https://api.etherscan.io/api?module=proxy&action=eth_call&to=0xb4a3c079acbd57668bf5292c13878f9225678381&data=0xbfafa378000000000000000000000000000000000000000000000000000000000000000${this.pool_id}000000000000000000000000${
                        Datas.eth_wallet.substring(2)
                    }&tag=latest&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"
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
                val nb = startNum.size
                this.my_stake = nb - 2
            } catch (cause: Throwable) {
                Log.e("Custom", "Error Origin.GetMyStake: $cause")
            }
            this.my_stake_running = false
        }
    }
}

class Genesis {
    companion object {
        val address = "bc1p94llwnug0zv9zvk8lj9g43s6ul5nssf9yl5pn8sdlyqj3rdy90qq34ck00"
        val pool_id = 2
        var my_stake = 0
        var all_stake = 1
        var earnings: Double = 0.0
        var pool_earnings = 0.0
        var my_stake_running = false

        fun GetPoolEarnings(){
            while (this.my_stake_running) { Thread.sleep(100) }
            if (this.my_stake > 0) {
                try {
                    val json = Url2Json("https://cruxpool.com/api/btc/miner/${this.address}")
                    val response = JSONObject(json)
                    val data = response.getJSONObject("data")
                    val perMin = data.getDouble("coinPerMins")
                    val perDay = perMin * 60 * 24
                    this.pool_earnings = perDay
                } catch (cause: Throwable) {
                    Log.e("Custom", "Error in Genesis.GetPoolEarnings: $cause")
                }
            }
        }

        fun GetMyEarnings(): Float {
            try {
                this.earnings = this.pool_earnings / this.all_stake * this.my_stake
            }
            catch (cause: Throwable) {
                Log.e("Custom","Error in Genesis.GetMyEarnings: $cause")
            }
            return this.earnings.toFloat()
        }

        fun GetAllStake() {
            while (this.my_stake_running) { Thread.sleep(100) }
            if (this.my_stake > 0) {
                try {
                    val url =
                        "https://api.etherscan.io/api?module=proxy&action=eth_call&to=0xb4a3c079acbd57668bf5292c13878f9225678381&data=0x03501951000000000000000000000000000000000000000000000000000000000000000${this.pool_id}&tag=latest&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"
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
                    val startNum = start.map { it.toInt(16) }
                    val nb = startNum.size
                    this.all_stake = nb - 2
                } catch (cause: Throwable) {
                    Log.e("Custom", "Error Genesis.GetAllStake: $cause")
                }
            }
        }

        fun GetMyStake() {
            this.my_stake_running = true
            try {
                val url =
                    "https://api.etherscan.io/api?module=proxy&action=eth_call&to=0xb4a3c079acbd57668bf5292c13878f9225678381&data=0xbfafa378000000000000000000000000000000000000000000000000000000000000000${this.pool_id}000000000000000000000000${
                        Datas.eth_wallet.substring(2)
                    }&tag=latest&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"
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
                val startNum = start.map { it.toInt(16) }
                val nb = startNum.size
                this.my_stake = nb - 2
            } catch (cause: Throwable) {
                Log.e("Custom", "Error Genesis.GetMyStake: $cause")
            }
            this.my_stake_running = false
        }
    }
}

class Northpool {
    companion object {
        val address = "bc1qyjp7kadrtr8j7gvvs9jej9c790jpmal4cwehle"
        val pool_id = 3
        var my_stake = 0
        var all_stake = 1
        var earnings: Double = 0.0
        var pool_earnings = 0.0
        var my_stake_running = false

        fun GetPoolEarnings(){
            while (this.my_stake_running) { Thread.sleep(100) }
            if (this.my_stake > 0) {
                try {
                    val json = Url2Json("https://cruxpool.com/api/btc/miner/${this.address}")
                    val response = JSONObject(json)
                    val data = response.getJSONObject("data")
                    val perMin = data.getDouble("coinPerMins")
                    val perDay = perMin * 60 * 24
                    this.pool_earnings = perDay
                } catch (cause: Throwable) {
                    Log.e("Custom", "Error in Genesis.GetPoolEarnings: $cause")
                }
            }
        }

        fun GetMyEarnings(): Float {
            try {
                this.earnings = this.pool_earnings / this.all_stake * this.my_stake
            }
            catch (cause: Throwable) {
                Log.e("Custom","Error in Genesis.GetMyEarnings: $cause")
            }
            return this.earnings.toFloat()
        }

        fun GetAllStake() {
            while (this.my_stake_running) { Thread.sleep(100) }
            if (this.my_stake > 0) {
                try {
                    val url =
                        "https://api.etherscan.io/api?module=proxy&action=eth_call&to=0xb4a3c079acbd57668bf5292c13878f9225678381&data=0x03501951000000000000000000000000000000000000000000000000000000000000000${this.pool_id}&tag=latest&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"
                    val json: String = Url2Json(url)
                    val response = JSONObject(json)
                    var data = response.getString("result")
                    Log.e("Custom", "Northpool.GetAllStake: data=${data}")
                    data = data.substring(2) // Remove the first two characters

                    val start = mutableListOf<String>()
                    var index = 0
                    while (index < data.length) {
                        start.add(data.substring(index, minOf(index + 64, data.length)))
                        index += 64
                    }
                    val startNum = start.map { it.toInt(16) }
                    val nb = startNum.size
                    this.all_stake = nb - 2
                } catch (cause: Throwable) {
                    Log.e("Custom", "Error Northpool.GetAllStake: $cause")
                }
            }
        }

        fun GetMyStake() {
            this.my_stake_running = true
            try {
                val url =
                    "https://api.etherscan.io/api?module=proxy&action=eth_call&to=0xb4a3c079acbd57668bf5292c13878f9225678381&data=0xbfafa378000000000000000000000000000000000000000000000000000000000000000${this.pool_id}000000000000000000000000${
                        Datas.eth_wallet.substring(2)
                    }&tag=latest&apikey=ZS4NECH7KXSBFJCUTPAKBWXWSH1PSPVX72"

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
                val startNum = start.map { it.toInt(16) }
                val nb = startNum.size
                this.my_stake = nb - 2
            } catch (cause: Throwable) {
                Log.e("Custom", "Error Northpool.GetMyStake: $cause")
            }
            this.my_stake_running = false
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
        var next_payout = ""
        var days2payout = ""
        var MainRefreashRunning = false

        fun RefreshStake() {
            this.MainRefreashRunning = true
            Thread { LiveReward() }.start()
            Thread { TotalPayout() }.start()

            val my_stake_ThreadList: List<Thread> = listOf(
                Thread { Origin.GetMyStake() },
                Thread { Genesis.GetMyStake() },
                Thread { Northpool.GetMyStake() },
            )
            for (t in my_stake_ThreadList){ t.start() }
            for (t in my_stake_ThreadList){ t.join() }

            val ThreadList: List<Thread> = listOf(
                Thread { Origin.GetAllStake() },
                Thread { Genesis.GetAllStake() },
                Thread { Northpool.GetAllStake() },

                Thread { Origin.GetPoolEarnings() },
                Thread { Genesis.GetPoolEarnings() },
                Thread { Northpool.GetPoolEarnings() }
            )
            for (t in ThreadList){
                t.start()
            }
            for (t in ThreadList){
                t.join()
            }
            this.MainRefreashRunning = false
        }

        fun RefreshTextValue() {
            while (this.MainRefreashRunning) {
                Thread.sleep(100)
            }
            this.earnings = (Origin.GetMyEarnings() + Genesis.GetMyEarnings() + Northpool.GetMyEarnings())
        }
    }
}
