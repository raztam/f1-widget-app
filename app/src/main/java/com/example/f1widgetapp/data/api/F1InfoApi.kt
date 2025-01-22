package com.example.f1widgetapp.data.api

import com.example.f1widgetapp.data.modals.Driver
import retrofit2.http.Path
import retrofit2.http.GET

interface F1InfoApi {
    @GET("drivers")
    suspend fun getDrivers(): List<Driver>

    @GET("drivers/{id}")
    suspend fun getDriverById(@Path("id") id: Int): Driver?
}