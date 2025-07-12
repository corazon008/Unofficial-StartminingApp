package com.example.startmining.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.startmining.network.cruxpool.CruxpoolService
import com.example.startmining.network.cruxpool.balance.BalanceWrapper
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {


    private val _balance = MutableLiveData<BalanceWrapper>()
    val balance: LiveData<BalanceWrapper> get() = _balance

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadBalance(address: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                val result = CruxpoolService.getBalance(address)
                _balance.value = result
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}