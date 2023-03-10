package com.espt.jx

import android.app.Application
import androidx.room.Room

class App : Application() {

    companion object {
        lateinit var instance: App
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(this, AppDatabase::class.java, "RoomDatabase").build()
    }
}