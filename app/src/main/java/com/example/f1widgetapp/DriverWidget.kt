package com.example.f1widgetapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.f1widgetapp.data.api.Api
import com.example.f1widgetapp.data.repository.Repository
import com.example.f1widgetapp.data.room.AppDatabase
import kotlinx.coroutines.runBlocking

/**
 * Implementation of App Widget functionality.
 */
class DriverWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val repository = Repository(
            driverDao = AppDatabase.getDatabase(context).driverDao(),
            remoteDataSource = Api(),
            context = context
        )
        runBlocking {
            val selectedDriverNumber = repository.getSelectedDriverNumber()
            val driver = selectedDriverNumber?.let { repository.getDriverByNumber(selectedDriverNumber) }

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(
                    context,
                    appWidgetManager,
                    appWidgetId,
                    driver?.fullName ?: "No driver selected"
                )
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    driverName: String
) {
    val views = RemoteViews(context.packageName, R.layout.driver_widget)
    views.setTextViewText(R.id.appwidget_text, driverName)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}