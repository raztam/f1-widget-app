package com.example.f1widgetapp.data.api

import android.util.Log
import com.example.f1widgetapp.data.LocalDataSource
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.DriverStandingUpdate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Api : ApiInterface, KoinComponent {
    private val api: F1InfoApi by inject()
    private val localDriversData: LocalDataSource by inject()

    override suspend fun getDrivers(): List<Driver> {
        return try {
            val drivers = api.getDrivers().mrData.driverTable?.drivers ?: return emptyList()
            val localData = localDriversData.drivers.getAdditionalInfo()
            
            drivers.map { driver ->
                val additionalInfo = localData[driver.driverId]
                if (additionalInfo != null) {
                    driver.copy(
                        teamName = additionalInfo.teamName ?: driver.teamName,
                        teamColor = additionalInfo.teamColor ?: driver.teamColor,
                    )
                } else {
                    driver
                }
            }
        } catch (e: Exception) {
            Log.e("Api", "Error fetching drivers", e)
            emptyList()
        }
    }

    override suspend fun getDriversStandings():  List<DriverStandingUpdate> {
        return try {
            val response = api.getDriverStandings()
            val standingsList = response.mrData.standingsTable?.standingsLists?.firstOrNull()
                ?: return emptyList()

            standingsList.driverStandings.map { standing ->
                DriverStandingUpdate(
                    driverId = standing.driver.driverId,
                    position = standing.position,
                    points = standing.points
                )
            }
        } catch (e: Exception) {
            Log.e("Api", "Error fetching driver standings", e)
            emptyList()
        }
    }
}
