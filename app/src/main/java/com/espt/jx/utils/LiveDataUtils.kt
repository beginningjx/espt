package com.espt.jx.utils

import androidx.lifecycle.MutableLiveData

object LiveDataUtils {
    val mutableLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val collectActivity: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
}