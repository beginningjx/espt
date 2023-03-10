package com.espt.jx.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Data(
    // ID
    @PrimaryKey(autoGenerate = true) val data_id: Int?,
    // 简介
    @ColumnInfo val introduction: String,
    // 图片
    @ColumnInfo val picture: String,
    // 分类
    @ColumnInfo val category: String,
    // 金钱
    @ColumnInfo val money: String,
    // 用户ID
    @ColumnInfo val user_id: Int,
    // 昵称
    @ColumnInfo val qq_name: String,
    // 头像
    @ColumnInfo val qq_picture: String
)