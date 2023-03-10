package com.espt.jx.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CollectDao {

    @Query("SELECT * FROM collect where user_id = :mId and data_id = :uId")
    fun isCollect(mId: Int, uId: Int): Boolean

    /**
     * 根据用户ID查询出所有收藏的数据ID
     * @param [mId] m id
     * @return [List<Int>]
     */
    @Query("SELECT data_id FROM collect where user_id = :mId")
    fun getAllId(mId: Int): List<Int>

    @Insert
    fun insert(collect: Collect)

    @Query("delete from collect where user_id = :mId and data_id = :uId")
    fun delete(mId: Int, uId: Int)
}