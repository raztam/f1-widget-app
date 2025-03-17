package com.example.f1widgetapp.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import androidx.glance.appwidget.updateAll
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.widgets.DriverWidget
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

            // Update data from API
            repository.updateDriverStandings()

            // Force widget update for all widget types
            DriverWidget.updateAll(applicationContext)
            // Future widgets:
            // TeamWidget.updateAll(applicationContext)
            // RaceWidget.updateAll(applicationContext)

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
            // Create calendar instance for next Sunday at 6 PM
            val calendar = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                set(Calendar.HOUR_OF_DAY, 18) // 6 PM
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)

                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.WEEK_OF_YEAR, 1)
                }
            }

            // Calculate initial delay safely
            val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

            // Define constraints (ensure network availability)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Create a weekly periodic work request
            val request = PeriodicWorkRequestBuilder<UpdateWidgetsWorker>(7, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    30, TimeUnit.MINUTES
                )
                .build()

            // Enqueue the work request
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
