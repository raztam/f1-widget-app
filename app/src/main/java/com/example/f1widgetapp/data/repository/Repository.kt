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
        updateDriverStandings()
        return remoteDrivers
    }

    override suspend fun updateDriverStandings() {
        val standings = remoteDataSource.getDriversStandings()
        Log.d("Repository", "Updating driver standings: $standings")
        standings.forEach { update ->
            driverDao.updateDriverStandings(
                driverId = update.driverId,
                position = update.position,
                points = update.points
            )
        }
    }

    override suspend fun getDriverByNumber(number: String): Driver? {
        return driverDao.getDriverByNumber(number)
    }

    override fun saveDriverForWidget(driverNumber: String, widgetId: Int) {
        context.getSharedPreferences("widget_drivers", Context.MODE_PRIVATE)
            .edit()
            .putString("widget_$widgetId", driverNumber)
            .apply()
    }

    override suspend fun getDriverForWidget(widgetId: Int): Driver? {
        val sharedPreferences = context.getSharedPreferences("widget_drivers", Context.MODE_PRIVATE)
        val driverNumber = sharedPreferences.getString("widget_$widgetId", "")
        
        if (driverNumber.isNullOrEmpty()) {
            return null
        }
        
        return getDriverByNumber(driverNumber)
    }
}
