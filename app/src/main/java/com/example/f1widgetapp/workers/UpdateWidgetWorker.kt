package com.example.f1widgetapp.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import androidx.glance.appwidget.updateAll
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.widgets.DriverWidget
import com.example.f1widgetapp.widgets.DriverStandingsWidget
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import java.util.Calendar

class UpdateWidgetsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val repository: RepositoryInterface by inject()

    override suspend fun doWork(): Result {
        return try {
            Log.i(TAG, "Updating F1 data for all widgets...")

            // Get races (will fetch from API if local database is empty)
            val races = repository.getAllRaces()
            Log.i(TAG, "Loaded ${races.size} races for the season")

            // Get drivers (will fetch from API if local database is empty)
            val drivers = repository.getAllDrivers()
            Log.i(TAG, "Loaded ${drivers.size} drivers")

            // Update driver standings from API (always do this after races)
            repository.updateDriverStandings()

            // Force widget update for all widget types
            DriverWidget.updateAll(applicationContext)
            DriverStandingsWidget.updateAll(applicationContext)
            // Future widgets:
            // TeamWidget.updateAll(applicationContext)
            // RaceWidget.updateAll(applicationContext)

            // Schedule next update based on next event (sprint or race)
            val nextEvent = repository.getNextRaceEvent()
            if (nextEvent != null) {
                val (race, isSprint) = nextEvent
                val eventTime = if (isSprint) {
                    race.getSprintEndTimeMillis()!!
                } else {
                    race.getRaceEndTimeMillis()
                }

                val eventType = if (isSprint) "Sprint" else "Race"
                scheduleNextUpdate(applicationContext, eventTime)
                Log.i(TAG, "Next update scheduled for $eventType at ${race.raceName} on ${race.date}")
            } else {
                Log.w(TAG, "No upcoming events found")
            }

            Log.i(TAG, "All widget updates completed successfully.")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Widget update failed: ${e.localizedMessage}", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "UpdateWidgetsWorker"
        private const val UNIQUE_WORK_NAME = "update_f1_widgets_data"

        fun schedule(context: Context) {
            // Schedule initial work to fetch race schedule and set up updates
            val initialRequest = OneTimeWorkRequestBuilder<UpdateWidgetsWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "initial_race_fetch",
                ExistingWorkPolicy.REPLACE,
                initialRequest
            )
        }

        fun scheduleNextUpdate(context: Context, nextEventEndTime: Long) {
            val delay = nextEventEndTime - System.currentTimeMillis()

            if (delay <= 0) {
                // Event already finished, update immediately
                val immediateRequest = OneTimeWorkRequestBuilder<UpdateWidgetsWorker>()
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()

                WorkManager.getInstance(context).enqueueUniqueWork(
                    UNIQUE_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    immediateRequest
                )
            } else {
                // Schedule update for after the event
                val request = OneTimeWorkRequestBuilder<UpdateWidgetsWorker>()
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        30, TimeUnit.MINUTES
                    )
                    .build()

                WorkManager.getInstance(context).enqueueUniqueWork(
                    UNIQUE_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    request
                )
            }
        }
    }
}
