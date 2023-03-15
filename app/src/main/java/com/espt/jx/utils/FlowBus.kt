package com.espt.jx.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


object FlowBus {

    private lateinit var isFlow: String
    private val mutableSharedFlow = MutableSharedFlow<Any?>(0, 1)

    /**
     * 发送
     * @param [string] 字符串
     * @param [int] int
     */
    fun send(string: String, any: Any? = 0) {
        isFlow = string
        mutableSharedFlow.tryEmit(any)
    }

    /**
     * 收到
     * @param [string] 字符串
     * @param [callback] 回调
     */
    fun receive(string: String, callback: (Any?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            mutableSharedFlow.collect {
                if (isFlow == string) {
                    callback(it)
                    isFlow = ""
                }
            }
        }
    }
}