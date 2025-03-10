package com.example.f1widgetapp.data.api

import android.util.Log
import com.example.f1widgetapp.data.LocalDataSource
import com.example.f1widgetapp.data.modals.Driver
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Api : ApiInterface, KoinComponent {
    private val api: F1InfoApi by inject()
    private val localDriversData: LocalDataSource by inject()

    override suspend fun getDrivers(): List<Driver> {
        return try {
            val drivers = api.getDrivers().mrData.driverTable.drivers
            val localData = localDriversData.drivers.getAdditionalInfo()
            drivers.map { driver ->
                val additionalInfo = localData[driver.driverId]
                if (additionalInfo != null) {
                    driver.copy(
                        teamName = additionalInfo.teamName ?: driver.teamName,
                        teamColor = additionalInfo.teamColor ?: driver.teamColor,
                        imageUrl = additionalInfo.imageUrl ?: driver.imageUrl
                    )
                } else {
                    driver
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
