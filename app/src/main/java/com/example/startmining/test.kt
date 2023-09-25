
import android.util.Log
import com.example.startmining.Datas
import com.example.startmining.Url2Json
import org.json.JSONObject

class Northpool {
    companion object {
        val address = "bc1qyjp7kadrtr8j7gvvs9jej9c790jpmal4cwehle"
        val pool_id = 3
        var my_stake = 0
        var all_stake = 0
        var earnings: Double = 0.0

        fun GetEarnings(): Float {
            try {
                var json = Url2Json("https://cruxpool.com/api/btc/miner/${this.address}")
                val response = JSONObject(json)
                val data = response.getJSONObject("data")
                val perMin = data.getDouble("coinPerMins")
                var perDay = perMin * 60 * 24
                this.earnings = perDay/ this.all_stake * this.my_stake
                Log.e(String(),"Northpool.earnings : ${this.earnings} ${this.all_stake} ${this.my_stake}")
            }
            catch (cause: Throwable) {
                Log.e(String(),"Error Northpool.GetEarnings: $cause")
            }
            return this.earnings.toFloat()
        }

        fun GetAllStake() {
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
                Log.e(String(), "Error Northpool.GetAllStake: $cause")
            }
        }

        fun GetMyStake() {
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
                Log.e(String(), "Error Northpool.GetMyStake: $cause")
            }
        }
    }
}

fun main(){
    Northpool.GetMyStake()
    Northpool.GetAllStake()
    println("${Northpool.my_stake}, ${Northpool.all_stake}")
}