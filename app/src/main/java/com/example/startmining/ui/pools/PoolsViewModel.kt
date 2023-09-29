package com.example.startmining.ui.pools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PoolsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is pools Fragment"
    }
    val text: LiveData<String> = _text
}