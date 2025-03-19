package com.example.f1widgetapp.data.repository

import com.example.f1widgetapp.data.modals.Driver


interface RepositoryInterface {
    suspend fun getAllDrivers(): List<Driver>
    suspend fun upsertDrivers(): List<Driver>
    suspend fun getDriverByNumber(number: String): Driver?

    suspend fun updateDriverStandings()


    fun saveDriverForWidget(driverNumber: String, widgetId: Int)
    suspend fun getDriverForWidget(widgetId: Int): Driver?

}