package com.jasontsh.interviewkickstart.loadingexample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel()  {
    val data:MutableLiveData<DoubleArray> by lazy {
        MutableLiveData(DoubleArray(0))
    }

    val failure : MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
}