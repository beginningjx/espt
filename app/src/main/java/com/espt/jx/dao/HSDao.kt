package com.espt.jx.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HSDao {

    /**
     * 获取所有数据
     * @return [List<HS>]
     */
    @Query("select * from hs")
    fun getAll(): List<HS>

    @Insert
    fun insert(hs: HS)

    /**
     * 根据历史删除对应数据
     * @param [mHistory] 历史
     */
    @Query("delete from hs where history = :mHistory")
    fun deleteHistoryData(mHistory: String)

    /**
     * 删除所有数据
     */
    @Query("delete from hs")
    fun deleteAllData()

}