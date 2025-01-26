package com.example.f1widgetapp.data.api

import android.util.Log
import com.example.f1widgetapp.data.modals.Driver
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class Api : ApiInterface  {

    private val api: F1InfoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openf1.org/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(F1InfoApi::class.java)
    }

    override suspend fun getDrivers(): List<Driver> {
        return try {
            api.getDrivers()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getDriverById(id: Int): Driver? {
        TODO("Not yet implemented")
    }
}
