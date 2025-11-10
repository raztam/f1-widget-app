package com.example.f1widgetapp.data.repository

import android.content.Context
import android.util.Log
import com.example.f1widgetapp.data.api.ApiInterface
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.Race
import com.example.f1widgetapp.data.room.DriverDao
import com.example.f1widgetapp.data.room.RaceDao
import java.util.Calendar

class Repository(
    private val driverDao: DriverDao,
    private val raceDao: RaceDao,
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

    override suspend fun fetchAndSaveRaceSchedule() {
        val races = remoteDataSource.getRaceSchedule()
        raceDao.upsertRaces(races)
        // Clean up old seasons
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        raceDao.deleteOldSeasons(currentYear)
    }

    override suspend fun getNextRaceEvent(): Pair<Race, Boolean>? {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        val races = raceDao.getRacesForSeason(currentYear)
        val now = System.currentTimeMillis()

        // Check all upcoming events (both sprints and races)
        for (race in races) {
            // Check if sprint is next
            if (race.hasSprint()) {
                val sprintEnd = race.getSprintEndTimeMillis()
                if (sprintEnd != null && sprintEnd > now) {
                    return Pair(race, true) // Next event is a sprint
                }
            }

            // Check if race is next
            val raceEnd = race.getRaceEndTimeMillis()
            if (raceEnd > now) {
                return Pair(race, false) // Next event is the main race
            }
        }

        return null // No upcoming events
    }

        override suspend fun getAllRaces(): List<Race> {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
            val races = raceDao.getRacesForSeason(currentYear)
            if (races.isEmpty()) {
                fetchAndSaveRaceSchedule()
                return raceDao.getRacesForSeason(currentYear)
            }
            return races
        }
    }

