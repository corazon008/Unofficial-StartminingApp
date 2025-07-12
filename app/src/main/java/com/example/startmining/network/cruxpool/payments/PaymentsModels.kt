package com.example.startmining.network.cruxpool.payments

import kotlinx.serialization.Serializable

@Serializable
data class PaymentsWrapper(
    val status: Boolean,
    val data: PaymentsData
)

@Serializable
data class PaymentsData(
    val payments: List<Payment>
)

@Serializable
data class Payment(
    val timestamp: Long,
    val amount: Int,
    val tx: String
)
