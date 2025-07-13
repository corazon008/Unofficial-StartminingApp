package com.example.startmining.network.cruxpool.pool

import kotlinx.serialization.Serializable

@Serializable
data class PoolsWrapper(
    val status: Boolean,
    val data: PoolsData
)

@Serializable
data class PoolsData(
    val avgHashrate: Double,
    val coinPerMins: Double,
    val hashrate: Double,
    val realtimeHashrate: Double,
    val reportedHashrate: Long
)
