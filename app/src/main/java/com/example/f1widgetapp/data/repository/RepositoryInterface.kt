package com.example.f1widgetapp.data.repository

import com.example.f1widgetapp.data.modals.Constructor
import com.example.f1widgetapp.data.modals.ConstructorStandingsWidgetSettings
import com.example.f1widgetapp.data.modals.ConstructorWidgetSettings
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.DriverStandingsWidgetSettings
import com.example.f1widgetapp.data.modals.Race
import com.example.f1widgetapp.data.modals.WidgetSettings


interface RepositoryInterface {
    suspend fun getAllDrivers(): List<Driver>
    suspend fun upsertDrivers(): List<Driver>
    suspend fun getDriverByNumber(number: String): Driver?

    suspend fun updateDriverStandings()

    suspend fun getAllConstructors(): List<Constructor>
    suspend fun upsertConstructors(): List<Constructor>
    suspend fun getConstructorById(id: String): Constructor?
    suspend fun updateConstructorStandings()

    suspend fun fetchAndSaveRaceSchedule()
    suspend fun getNextRaceEvent(): Pair<Race, Boolean>?
    suspend fun getAllRaces(): List<Race>

    fun saveWidgetSettings(settings: WidgetSettings, widgetId: Int)
    suspend fun getWidgetSettings(widgetId: Int): WidgetSettings

    fun saveConstructorWidgetSettings(settings: ConstructorWidgetSettings, widgetId: Int)
    suspend fun getConstructorWidgetSettings(widgetId: Int): ConstructorWidgetSettings

    fun saveDriverStandingsWidgetSettings(settings: DriverStandingsWidgetSettings, widgetId: Int)
    suspend fun getDriverStandingsWidgetSettings(widgetId: Int): DriverStandingsWidgetSettings

    fun saveConstructorStandingsWidgetSettings(settings: ConstructorStandingsWidgetSettings, widgetId: Int)
    suspend fun getConstructorStandingsWidgetSettings(widgetId: Int): ConstructorStandingsWidgetSettings
}