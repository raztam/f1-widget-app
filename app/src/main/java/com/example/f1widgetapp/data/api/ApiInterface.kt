package com.example.f1widgetapp.data.api

import com.example.f1widgetapp.data.modals.Constructor
import com.example.f1widgetapp.data.modals.ConstructorStandingUpdate
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.DriverStandingUpdate
import com.example.f1widgetapp.data.modals.Race

interface ApiInterface {
    suspend fun getDrivers(): List<Driver>

    suspend fun getDriversStandings(): List<DriverStandingUpdate>

    suspend fun getConstructors(): List<Constructor>

    suspend fun getConstructorsStandings(): List<ConstructorStandingUpdate>

    suspend fun getRaceSchedule(): List<Race>
}