package com.example.f1widgetapp.data.api

import android.util.Log
import com.example.f1widgetapp.data.modals.Driver
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class Api : ApiInterface  {

    private val api: F1InfoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jolpi.ca/ergast/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(F1InfoApi::class.java)
    }

    override suspend fun getDrivers(): List<Driver> {
        return try {
            val drivers =  api.getDrivers().mrData.driverTable.drivers
            drivers
        } catch (e: Exception) {
            emptyList()
        }
    }


}
