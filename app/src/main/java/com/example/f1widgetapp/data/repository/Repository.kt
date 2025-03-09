package com.example.f1widgetapp.data.repository

import android.content.Context
import android.util.Log
import com.example.f1widgetapp.data.api.ApiInterface
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.room.DriverDao

class Repository(
    private val driverDao: DriverDao,
    private val remoteDataSource: ApiInterface,
    private val context: Context
) : RepositoryInterface {
    override suspend fun getAllDrivers(): List<Driver> {
        val localDrivers = driverDao.getAllDrivers()
        return localDrivers.ifEmpty {
            upsertDrivers()
        }
    }

    override suspend fun upsertDrivers(): List<Driver> {
        val remoteDrivers = remoteDataSource.getDrivers()
        driverDao.upsertDrivers(remoteDrivers)
        return remoteDrivers
    }

    override suspend fun getDriverByNumber(number: String): Driver? {
        return driverDao.getDriverByNumber(number)
    }

    override fun saveSelectedDriverNumber(driverNumber: String) {
        context.getSharedPreferences("f1_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("selected_driver_number", driverNumber)
            .apply()
    }

    override suspend fun getSelectedDriver(): Driver? {
        val sharedPreferences = context.getSharedPreferences("f1_prefs", Context.MODE_PRIVATE)
        val selectedNumber = sharedPreferences.getString("selected_driver_number", "")
        return if (selectedNumber.isNullOrEmpty()) {
            null
        } else {
            getDriverByNumber(selectedNumber)
        }
    }
}