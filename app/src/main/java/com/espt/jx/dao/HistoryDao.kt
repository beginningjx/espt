package com.espt.jx.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    /**
     * 根据用户ID查询出所有收藏的数据ID
     * @param [mId] m id
     * @return [List<Int>]
     */
    @Query("SELECT data_id FROM history where user_id = :mId")
    fun getAllId(mId: Int): List<Int>

    @Insert
    fun insert(history: History)

    @Query("delete from history where user_id = :mId and data_id = :uId")
    fun delete(mId: Int, uId: Int)
}