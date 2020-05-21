package com.example.andysoft.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [(Book::class)],version = 1)
abstract class AppDb : RoomDatabase(){

    abstract fun bookDao(): BookDao

    companion object {
        private var INSTANCE: AppDb? = null
        fun getInstance(context: Context): AppDb {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDb::class.java,
                    "BookDB")
                    .build()
            }
            return INSTANCE as AppDb
        }
    }
}