package com.example.f1widgetapp.data.api

import android.util.Log
import com.example.f1widgetapp.data.LocalDataSource
import com.example.f1widgetapp.data.modals.Constructor
import com.example.f1widgetapp.data.modals.ConstructorStandingUpdate
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.DriverStandingUpdate
import com.example.f1widgetapp.data.modals.Race
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Api : ApiInterface, KoinComponent {
    private val api: F1InfoApi by inject()
    private val localDriversData: LocalDataSource by inject()

    override suspend fun getDrivers(): List<Driver> {
        return try {
            val drivers = api.getDrivers().mrData.driverTable?.drivers
                ?.filter { it.driverNumber != null }
                ?: return emptyList()
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

            (standingsList.driverStandings ?: emptyList()).map { standing ->
                DriverStandingUpdate(
                    driverId = standing.driver.driverId,
                    position = standing.position ?: "DNF",
                    points =  standing.points ?: "0"
                )
            }
        } catch (e: Exception) {
            Log.e("F1Api", "Error fetching driver standings", e)
            emptyList()
        }
    }

    override suspend fun getConstructors(): List<Constructor> {
        return try {
            val constructors = api.getConstructors().mrData.constructorTable?.constructors ?: return emptyList()
            val localData = localDriversData.constructors.getAdditionalInfo()

            constructors.map { ref ->
                val additionalInfo = localData[ref.constructorId]
                Constructor(
                    constructorId = ref.constructorId,
                    name = ref.name,
                    nationality = ref.nationality,
                    url = ref.url,
                    teamColor = additionalInfo?.teamColor
                )
            }
        } catch (e: Exception) {
            Log.e("Api", "Error fetching constructors", e)
            emptyList()
        }
    }

    override suspend fun getConstructorsStandings(): List<ConstructorStandingUpdate> {
        return try {
            val response = api.getConstructorStandings()
            val standingsList = response.mrData.standingsTable?.standingsLists?.firstOrNull()
                ?: return emptyList()

            (standingsList.constructorStandings ?: emptyList()).map { standing ->
                ConstructorStandingUpdate(
                    constructorId = standing.constructor.constructorId,
                    position = standing.position ?: "DNF",
                    points = standing.points ?: "0"
                )
            }
        } catch (e: Exception) {
            Log.e("Api", "Error fetching constructor standings", e)
            emptyList()
        }
    }

    override suspend fun getRaceSchedule(): List<Race> {
        return try {
            val response = api.getRaceSchedule()
            response.mrData.raceTable.races.map { raceResponse ->
                Race(
                    round = raceResponse.round,
                    season = raceResponse.season,
                    raceName = raceResponse.raceName,
                    date = raceResponse.date,
                    time = raceResponse.time,
                    circuitName = raceResponse.circuit?.circuitName,
                    sprintDate = raceResponse.sprint?.date,
                    sprintTime = raceResponse.sprint?.time
                )
            }
        } catch (e: Exception) {
            Log.e("Api", "Error fetching race schedule", e)
            emptyList()
        }
    }
}

