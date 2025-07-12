package com.example.startmining.network.cruxpool.balance

import kotlinx.serialization.Serializable

@Serializable
data class BalanceWrapper(
    val status: Boolean,
    val data: BalanceData
)

@Serializable
data class BalanceData(
    val balance: Double
)
