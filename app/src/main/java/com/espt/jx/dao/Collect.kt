package com.espt.jx.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Collect(
    @PrimaryKey val id: Int?,
    // 用户ID
    @ColumnInfo val user_id: Int,
    // 数据ID
    @ColumnInfo val data_id: Int,
)
