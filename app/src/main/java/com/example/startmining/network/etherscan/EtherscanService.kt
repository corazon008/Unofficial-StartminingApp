package com.example.startmining.network.etherscan

import android.util.Log
import com.example.startmining.build_eth_url
import com.example.startmining.network.HttpManager

object EtherscanService {

    suspend fun getUserStakedNft(poolId: Int, ethAddress: String): Int {
        val url = build_eth_url(
            "data" to "0xbfafa378000000000000000000000000000000000000000000000000000000000000000${poolId}000000000000000000000000${
                ethAddress.substring(2)
            }"
        )

        val nbStakedNftWrapper = HttpManager.get(url, NbStakedNftWrapper.serializer())
        val data =
            nbStakedNftWrapper.result.substring(2) // Remove the first two characters - i.e. "0x"
        val nft = mutableListOf<String>()
        var index = 0
        while (index < data.length) {
            nft.add(data.substring(index, minOf(index + 64, data.length)))
            index += 64
        }

        val size = nft.size - 2 // because it seems that there is always 2 more elements
        if (size < 0) {
            Log.e("EtherscanService", "getUserStakedNft for $poolId: Error parsing staked NFT data: data=${nbStakedNftWrapper.result} url=${url}")
            return 0
        }

        Log.i("EtherscanService", "getUserStakedNft for $poolId with $size staked NFT url=${url}")

        return size
    }

    suspend fun getAllStakedNft(poolId: Int): Int {
        val url =
            build_eth_url("data" to "0x03501951000000000000000000000000000000000000000000000000000000000000000${poolId}")

        val allStakedNftWrapper = HttpManager.get(url, NbStakedNftWrapper.serializer())
        val data =
            allStakedNftWrapper.result.substring(2) // Remove the first two characters - i.e. "0x"
        val nft = mutableListOf<String>()
        var index = 0
        while (index < data.length) {
            nft.add(data.substring(index, minOf(index + 64, data.length)))
            index += 64
        }

        val size = nft.size - 2 // because it seems that there is always 2 more elements
        if (size < 0) {
            Log.e("EtherscanService", "getAllStakedNft for $poolId: Error parsing staked NFT data: data=${allStakedNftWrapper.result} url=${url}")
            return 0
        }

        Log.i("EtherscanService", "getAllStakedNft for $poolId with $size staked NFT url=${url}")

        return size
    }
}