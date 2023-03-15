package com.espt.jx.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HS(
    // ID
    @PrimaryKey val id: Int,
    // 简介
    @ColumnInfo val history: String,
)
