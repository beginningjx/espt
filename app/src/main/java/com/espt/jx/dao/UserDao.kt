package com.espt.jx.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    /**
     * 根据用户ID获取用户数据
     * @param [mId] 用户ID
     * @return [User]
     */
    @Query("SELECT * FROM user where id = :mId")
    fun getIdUser(mId: Int): User

    /**
     * 根据用户QQ号获取用户数据
     * @param [mq] qq号
     * @return [User]
     */
    @Query("SELECT * FROM user where qq = :mq")
    fun getQQUser(mq: String): User

    /**
     * 根据QQ号判断数据库中有没有重复的QQ号
     * @param [mq] qq号
     * @return [Boolean]
     */
    @Query("SELECT qq FROM user where qq = :mq")
    fun isQQ(mq: String): Boolean

    /**
     * 登录
     * @param [mq] qq号
     * @param [mp] 密码
     * @return [Boolean]
     */
    @Query("SELECT qq,password FROM user where qq = :mq and password = :mp")
    fun login(mq: String, mp: String): Boolean

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}