package com.example.startmining.ui.simulator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimulatorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is simulator Fragment"
    }
    val text: LiveData<String> = _text
}