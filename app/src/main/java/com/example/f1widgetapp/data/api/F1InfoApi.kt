package com.example.f1widgetapp.data.api

import com.example.f1widgetapp.data.modals.ErgastResponse
import com.example.f1widgetapp.data.modals.RaceScheduleResponse
import retrofit2.http.GET

interface F1InfoApi {
    @GET("f1/current/drivers")
    suspend fun getDrivers(): ErgastResponse

    @GET("f1/current/driverstandings")
    suspend fun getDriverStandings(): ErgastResponse

    @GET("f1/current/races.json")
    suspend fun getRaceSchedule(): RaceScheduleResponse

}