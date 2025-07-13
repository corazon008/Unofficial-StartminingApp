package com.example.startmining.network.pools

data class PoolInfo (
    val address: String,
    val poolId: Int,
    var nbStakedNftUser: Int = 0,
    var nbStakedNft: Int = 0,
    var userEarnings: Double = 0.0,
    var poolEarnings: Double = 0.0,
    var hashrate: Double = 0.0,
)