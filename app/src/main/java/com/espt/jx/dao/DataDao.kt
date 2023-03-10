package com.espt.jx.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDao {
    /**
     * 获取所有数据
     * @return [List<Data>]
     */
    @Query("SELECT * FROM data")
    fun getAll(): List<Data>

    /**
     * 根据数据ID查找对应数据
     * @param [mId] m id
     * @return [Data]
     */
    @Query("SELECT * FROM data where data_id = :mId")
    fun getDataId(mId: Int): Data

    /**
     * 根据数据ID查找对应数据
     * @param [mId] m id
     * @return [List<Data>]
     */
    @Query("SELECT * FROM data where data_id in (:mId)")
    fun getDataIds(mId: List<Int>): List<Data>

    @Insert
    fun insert(data: Data)

    @Delete
    fun delete(data: Data)
}