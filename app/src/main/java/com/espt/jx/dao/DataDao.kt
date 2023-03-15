package com.espt.jx.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDao {
    /**
     * 获取所有数据
     * @return [List<Data>]
     */
    @Query("select * from data")
    fun getAll(): List<Data>

    /**
     * 根据数据ID查找对应数据
     * @param [mId] m id
     * @return [Data]
     */
    @Query("select * from data where data_id = :mId")
    fun getDataId(mId: Int): Data

    /**
     * 根据用户ID查找对应数据
     * @param [mId] 用户ID
     * @return [Data]
     */
    @Query("select * from data where user_id = :mId")
    fun getUserId(mId: Int): List<Data>

    /**
     * 根据数据ID查找对应数据
     * @param [mId] id
     * @return [List<Data>]
     */
    @Query("select * from data where data_id in (:mId)")
    fun getDataIds(mId: List<Int>): List<Data>


    /**
     * 根据分类查找对应数据
     * @param [mCategory] 分类
     * @return [List<Data>]
     */
    @Query("select * from data where category = :mCategory")
    fun getDataCategory(mCategory: String): List<Data>


    /**
     * 根据简介模糊查找
     * @param [mIntroduction] 介绍
     * @return [List<Data>]
     */
    @Query("select * from data where introduction like '%' || :mIntroduction || '%' or category like '%' || :mIntroduction || '%'")
    fun getIntroductionLike(mIntroduction: String): List<Data>

    @Insert
    fun insert(data: Data)

    /**
     * 根据数据ID删除对应数据
     * @param [dId] d id
     */
    @Query("delete from data where data_id = :dId")
    fun delete(dId: Int)

    /**
     * 根据用户ID删除对应数据
     */
    @Query("delete from data where user_id = :mId")
    fun deleteAll(mId: Int)
}