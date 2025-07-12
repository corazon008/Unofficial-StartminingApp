package com.example.startmining.network.cruxpool

import com.example.startmining.network.HttpManager
import com.example.startmining.network.cruxpool.balance.BalanceWrapper
import com.example.startmining.network.cruxpool.payments.PaymentsWrapper

object CruxpoolService {

    suspend fun getBalance(address: String): BalanceWrapper {
        return HttpManager.get("https://cruxpool.com/api/btc/miner/$address/balance", BalanceWrapper.serializer())
    }

    fun getBalance1(address: String): BalanceWrapper {
        return HttpManager.get1("https://cruxpool.com/api/btc/miner/$address/balance", BalanceWrapper.serializer())
    }

    suspend fun getPayments(address: String): PaymentsWrapper {
        return HttpManager.get("https://cruxpool.com/api/btc/miner/$address/payments", PaymentsWrapper.serializer())
    }
}