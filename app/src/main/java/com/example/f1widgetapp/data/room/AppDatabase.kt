package com.example.f1widgetapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.f1widgetapp.data.modals.Constructor
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.Race


@Database(entities = [Driver::class, Race::class, Constructor::class], version = 11)
abstract class AppDatabase : RoomDatabase() {
    abstract fun driverDao(): DriverDao
    abstract fun raceDao(): RaceDao
    abstract fun constructorDao(): ConstructorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "f1_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}