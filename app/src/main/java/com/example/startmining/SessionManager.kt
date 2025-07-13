package com.example.startmining

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.startmining.network.cruxpool.CruxpoolService
import com.example.startmining.network.cruxpool.balance.BalanceWrapper
import com.example.startmining.network.pools.PoolInfo
import com.example.startmining.network.pools.PoolsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.pow

object SessionManager {
    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    private val _payments = MutableLiveData<Double>()
    val payments: LiveData<Double> get() = _payments

    private val _userEarnings = MutableLiveData<Double>()
    val userEarnings: LiveData<Double> get() = _userEarnings

    private val _poolListInfo = MutableLiveData<List<PoolInfo>>()
    val poolListInfo: LiveData<List<PoolInfo>> get() = _poolListInfo

    /**
     * Update the user's balance by fetching it from the Cruxpool API.
     * @param address The user's address to fetch balance information.
     */
    fun updateBalance(address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val balanceWrapper: BalanceWrapper = CruxpoolService.getBalance(address)
                _balance.postValue(balanceWrapper.data.balance / 10.0.pow(8.0))
            } catch (e: Exception) {
                Log.e("SessionManager", "Error fetching balance: ${e.message}")
            }
        }
    }

    /**
     * Update the user's payments and calculate the total amount received.
     * @param address The user's address to fetch payment information.
     */
    fun updatePayments(address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val paymentsWrapper = CruxpoolService.getPayments(address)

                var totalAmount: Int = 0
                for (payment in paymentsWrapper.data.payments) {
                    totalAmount += payment.amount
                    Log.i("Payment", payment.toString())
                }

                _payments.postValue(totalAmount / 10.0.pow(8.0))
            } catch (e: Exception) {
                Log.e("SessionManager", "Error fetching payments: ${e.message}")
            }
        }
    }

    /**
     * Update the list of pools and calculate user earnings based on the pools.
     * @param address The user's address to fetch pool information.
     */
    fun updatePoolsInfo(address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val poolsInfo = PoolsService.updatePoolsInfo(address)
            _poolListInfo.postValue(poolsInfo)
            for (pool in poolsInfo) {
                val currentEarnings = _userEarnings.value ?: 0.0
                _userEarnings.postValue(currentEarnings + pool.userEarnings)
            }
        }
    }
}