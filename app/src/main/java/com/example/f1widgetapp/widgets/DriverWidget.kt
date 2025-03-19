package com.example.f1widgetapp.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.example.f1widgetapp.activities.DriverWidgetSettingsActivity
import com.example.f1widgetapp.composables.DriverCard
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.workers.UpdateWidgetsWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DriverWidget : GlanceAppWidget(), KoinComponent {
    private val repository: RepositoryInterface by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val selectedDriver = repository.getDriverForWidget(widgetId)

        Log.d("MyDriverWidget", "Driver for widget ID $widgetId: $selectedDriver")

        val settingsIntent = Intent(context, DriverWidgetSettingsActivity::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }

        provideContent {
            DriverWidgetContent(
                driver = selectedDriver,
                onClick = actionStartActivity(settingsIntent)
            )
        }
    }
}

@Composable
fun DriverWidgetContent(driver: Driver?, onClick: Action) {
    DriverCard(
        driver = driver,
        modifier = GlanceModifier.clickable(onClick)
    )
}

class DriverWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = DriverWidget

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Schedule updates when widget is added to home screen
        UpdateWidgetsWorker.schedule(context)
    }
}