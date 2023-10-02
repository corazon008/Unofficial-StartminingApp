package com.example.startmining.ui.pools

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.Datas
import com.example.startmining.RoundHashrate
import com.example.startmining.Url2Json
import com.example.startmining.databinding.FragmentOriginBinding
import org.json.JSONObject

class OriginFragment : Fragment() {
    private var _binding: FragmentOriginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOriginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Origin.WaitUntilReady()
        binding.occupancy.text = Origin.all_stake.toString()
        binding.hashrate.text = RoundHashrate(Origin.hashrate)
        binding.btcEarnings.text = Origin.pool_earnings.toString()
        binding.myStake.text = Origin.my_stake.toString()
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_origin, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class Origin {
    companion object {
        val address = "bc1qdzcgvennnjzv4jry38s0krjtl3x9n374302c75"
        val pool_id = 1
        var my_stake = 0
        var all_stake = 1
        var my_earnings: Double = 0.0
        var pool_earnings = 0.0
        var hashrate = 0.0
        val my_stake_thread = Thread { this.GetMyStake() }
        val all_stake_thread = Thread { this.GetAllStake() }
        val pool_earnings_thread = Thread { this.GetPoolEarnings() }

        fun WaitUntilReady() {
            this.my_stake_thread.join()
            this.all_stake_thread.join()
            this.pool_earnings_thread.join()
        }

        fun GetStats() {
            this.my_stake_thread.start()
            this.my_stake_thread.join()
            if (this.my_stake > 0) {
                this.all_stake_thread.start()
                this.pool_earnings_thread.start()
                this.all_stake_thread.join()
                this.pool_earnings_thread.join()
            } else {
                this.all_stake_thread.start()
                this.pool_earnings_thread.start()
            }
        }

        fun GetPoolEarnings() {
            this.my_stake_thread.join()
            try {
                val json = Url2Json("https://cruxpool.com/api/btc/miner/${this.address}")
                val response = JSONObject(json)
                val data = response.getJSONObject("data")
                val perMin = data.getDouble("coinPerMins")
                val perDay = perMin * 60 * 24
                this.pool_earnings = perDay
                this.hashrate = data.getDouble("avgHashrate")
            } catch (cause: Throwable) {
                Log.e("Custom", "Error in Origin.GetPoolEarnings: $cause")
            }

        }

        fun GetMyEarnings(): Float {
            try {
                this.my_earnings = this.pool_earnings / this.all_stake * this.my_stake
            } catch (cause: Throwable) {
                Log.e("Custom", "Error in Origin.GetMyEarnings: $cause")
            }
            return this.my_earnings.toFloat()
        }

        fun GetAllStake() {
            this.my_stake_thread.join()
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
                Log.e("Custom", "Error Origin.GetMyStake: $cause")
            }
        }
    }
}
