package com.example.f1widgetapp.data.repository

import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.Race
import com.example.f1widgetapp.data.modals.WidgetSettings


interface RepositoryInterface {
    suspend fun getAllDrivers(): List<Driver>
    suspend fun upsertDrivers(): List<Driver>
    suspend fun getDriverByNumber(number: String): Driver?

    suspend fun updateDriverStandings()

    suspend fun fetchAndSaveRaceSchedule()
    suspend fun getNextRaceEvent(): Pair<Race, Boolean>?
    suspend fun getAllRaces(): List<Race>

    fun saveWidgetSettings(settings: WidgetSettings, widgetId: Int)
    suspend fun getWidgetSettings(widgetId: Int): WidgetSettings

}