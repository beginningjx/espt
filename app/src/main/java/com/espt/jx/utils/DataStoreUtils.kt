package com.espt.jx.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.espt.jx.App
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object DataStoreUtils {

    // 创建DataStore
    private val App.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "DataName"
    )

    // DataStore变量
    private val dataStore = App.instance.dataStore

    /**
     * 存放Int数据
     */
    private suspend fun setIntData(key: String, value: Int) = dataStore.edit {
        it[intPreferencesKey(key)] = value
    }

    /**
     * 存放Long数据
     */
    private suspend fun setLongData(key: String, value: Long) = dataStore.edit {
        it[longPreferencesKey(key)] = value
    }

    /**
     * 存放String数据
     */
    private suspend fun setStringData(key: String, value: String) = dataStore.edit {
        it[stringPreferencesKey(key)] = value
    }

    /**
     * 存放Boolean数据
     */
    private suspend fun setBooleanData(key: String, value: Boolean) = dataStore.edit {
        it[booleanPreferencesKey(key)] = value
    }

    /**
     * 存放Float数据
     */
    private suspend fun setFloatData(key: String, value: Float) = dataStore.edit {
        it[floatPreferencesKey(key)] = value
    }

    /**
     * 存放Double数据
     */
    private suspend fun setDoubleData(key: String, value: Double) = dataStore.edit {
        it[doublePreferencesKey(key)] = value
    }

    /**
     * 取出Int数据
     */
    private fun getIntData(key: String, default: Int = 0): Int = runBlocking {
        return@runBlocking dataStore.data.map {
            it[intPreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 取出Long数据
     */
    private fun getLongData(key: String, default: Long = 0): Long = runBlocking {
        return@runBlocking dataStore.data.map {
            it[longPreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 取出String数据
     */
    private fun getStringData(key: String, default: String? = null): String = runBlocking {
        return@runBlocking dataStore.data.map {
            it[stringPreferencesKey(key)] ?: default
        }.first()!!
    }

    /**
     * 取出Boolean数据
     */
    private fun getBooleanData(key: String, default: Boolean = false): Boolean = runBlocking {
        return@runBlocking dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 取出Float数据
     */
    private fun getFloatData(key: String, default: Float = 0.0f): Float = runBlocking {
        return@runBlocking dataStore.data.map {
            it[floatPreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 取出Double数据
     */
    private fun getDoubleData(key: String, default: Double = 0.00): Double = runBlocking {
        return@runBlocking dataStore.data.map {
            it[doublePreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 存数据
     */
    fun <T> setData(key: String, value: T) {
        runBlocking {
            when (value) {
                is Int -> setIntData(key, value)
                is Long -> setLongData(key, value)
                is String -> setStringData(key, value)
                is Boolean -> setBooleanData(key, value)
                is Float -> setFloatData(key, value)
                is Double -> setDoubleData(key, value)
                else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
            }
        }
    }

    /**
     * 取数据
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: String, defaultValue: T): T {
        val data = when (defaultValue) {
            is Int -> getIntData(key, defaultValue)
            is Long -> getLongData(key, defaultValue)
            is String -> getStringData(key, defaultValue)
            is Boolean -> getBooleanData(key, defaultValue)
            is Float -> getFloatData(key, defaultValue)
            is Double -> getDoubleData(key, defaultValue)
            else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
        }
        return data as T
    }

    /**
     * 清空数据
     */
    fun clearData() = runBlocking { dataStore.edit { it.clear() } }

}