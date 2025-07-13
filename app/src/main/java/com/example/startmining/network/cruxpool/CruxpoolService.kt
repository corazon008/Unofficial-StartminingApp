package com.example.startmining.network.cruxpool

import com.example.startmining.network.HttpManager
import com.example.startmining.network.cruxpool.balance.BalanceWrapper
import com.example.startmining.network.cruxpool.payments.PaymentsWrapper
import com.example.startmining.network.cruxpool.pool.PoolsWrapper

object CruxpoolService {

    suspend fun getBalance(address: String): BalanceWrapper {
        return HttpManager.get("https://cruxpool.com/api/btc/miner/$address/balance", BalanceWrapper.serializer())
    }

    suspend fun getPayments(address: String): PaymentsWrapper {
        return HttpManager.get("https://cruxpool.com/api/btc/miner/$address/payments", PaymentsWrapper.serializer())
    }

    suspend fun getPoolsInfo(address: String): PoolsWrapper {
        return HttpManager.get("https://cruxpool.com/api/btc/miner/$address", PoolsWrapper.serializer())
    }
}