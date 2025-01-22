package com.example.f1widgetapp.data.repository

import android.util.Log
import com.example.f1widgetapp.data.api.ApiInterface
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.room.DriverDao

class Repository(
    private val driverDao: DriverDao,
    private val remoteDataSource: ApiInterface
) : RepositoryInterface {
    override suspend fun getAllDrivers(): List<Driver> {
        Log.d("F1AppTests", "Fetching all drivers")
        val localDrivers = driverDao.getAllDrivers()
        return localDrivers.ifEmpty {
            upsertDrivers()
        }
    }

    override suspend fun upsertDrivers(): List<Driver> {
        val remoteDrivers = remoteDataSource.getDrivers()
        Log.d("F1AppTests", "Upserting drivers: $remoteDrivers")
        driverDao.upsertDrivers(remoteDrivers)
        return remoteDrivers
    }
}