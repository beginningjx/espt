package com.espt.jx.dao


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    // ID
    @PrimaryKey(autoGenerate = true) val id: Int?,
    // 昵称
    @ColumnInfo val qq_name: String,
    // 密码
    @ColumnInfo val password: String,
    // QQ
    @ColumnInfo val qq: String,
    // 头像
    @ColumnInfo val qq_picture: String
)
