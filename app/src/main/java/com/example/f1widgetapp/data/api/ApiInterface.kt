package com.example.f1widgetapp.data.api

import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.DriverStandingUpdate
import com.example.f1widgetapp.data.modals.Race

interface ApiInterface {
    suspend fun getDrivers(): List<Driver>

    suspend fun getDriversStandings(): List<DriverStandingUpdate>

    suspend fun getRaceSchedule(): List<Race>
}