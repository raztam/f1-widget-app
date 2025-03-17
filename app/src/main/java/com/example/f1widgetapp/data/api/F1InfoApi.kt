package com.example.f1widgetapp.data.api

import com.example.f1widgetapp.data.modals.ErgastResponse
import retrofit2.http.GET

interface F1InfoApi {
    @GET("f1/current/drivers")
    suspend fun getDrivers(): ErgastResponse

    @GET("f1/current/driverstandings")
    suspend fun getDriverStandings(): ErgastResponse

}