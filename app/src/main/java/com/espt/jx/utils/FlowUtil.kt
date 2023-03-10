package com.espt.jx.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow


object FlowUtil {

    /*
    * @replay 缓存
    * @extraBufferCapacity 额外的缓冲容量
    * @BufferOverflow.SUSPEND：默认的缓存溢出策略，它的作用是在缓存区满了之后，挂起发射函数，直至缓存区中的数据被消费后，再恢复。
    * @BufferOverflow.DROP_OLDEST。它的作用是缓存区满了之后，丢弃最老的数据，以腾出空间来存储新数据。
    * @BufferOverflow.DROP_LATEST。它的作用是缓存区满了之后，丢弃新来的数据。
    */

    val uiState = MutableSharedFlow<Int>(0, 1, BufferOverflow.SUSPEND)
}