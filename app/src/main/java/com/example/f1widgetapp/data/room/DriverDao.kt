package com.example.f1widgetapp.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.f1widgetapp.data.modals.Driver

@Dao
interface DriverDao {
    @Upsert
    suspend fun upsertDrivers(drivers: List<Driver>)

    @Query("SELECT * FROM drivers")
    suspend fun getAllDrivers(): List<Driver>

    @Query("SELECT * FROM drivers WHERE driverNumber = :number")
    suspend fun getDriverByNumber(number: String): Driver?
}