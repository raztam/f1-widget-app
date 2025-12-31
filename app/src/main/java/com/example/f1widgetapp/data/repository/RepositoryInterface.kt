package com.example.f1widgetapp.data.repository

import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.Race


interface RepositoryInterface {
    suspend fun getAllDrivers(): List<Driver>
    suspend fun upsertDrivers(): List<Driver>
    suspend fun getDriverByNumber(number: String): Driver?

    suspend fun updateDriverStandings()

    suspend fun fetchAndSaveRaceSchedule()
    suspend fun getNextRaceEvent(): Pair<Race, Boolean>?
    suspend fun getAllRaces(): List<Race>

    fun saveDriverForWidget(driverNumber: String, widgetId: Int)
    suspend fun getDriverForWidget(widgetId: Int): Driver?

}