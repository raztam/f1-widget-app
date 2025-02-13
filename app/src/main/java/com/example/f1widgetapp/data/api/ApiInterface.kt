package com.example.f1widgetapp.data.api

import com.example.f1widgetapp.data.modals.Driver

interface ApiInterface {
    suspend fun getDrivers(): List<Driver>
}