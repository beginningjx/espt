package com.espt.jx

import androidx.room.Database
import androidx.room.RoomDatabase
import com.espt.jx.dao.*

@Database(
    entities = [Data::class, User::class, Collect::class, History::class, Publish::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
    abstract fun userDao(): UserDao
    abstract fun collectDao(): CollectDao
    abstract fun historyDao(): HistoryDao
    abstract fun publishDao(): PublishDao
}