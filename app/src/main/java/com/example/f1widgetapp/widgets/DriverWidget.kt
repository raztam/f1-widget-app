package com.example.f1widgetapp.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.f1widgetapp.activities.DriverWidgetSettingsActivity
import com.example.f1widgetapp.composables.DriverCard
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.workers.UpdateWidgetsWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DriverWidget : GlanceAppWidget(), KoinComponent {
    private val repository: RepositoryInterface by inject()

    // Add state definition to enable force updates
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)

        provideContent {
            val prefs = currentState<Preferences>()
            val driverNumber = prefs[stringPreferencesKey("driver_number")] ?: ""
            val transparency = prefs[floatPreferencesKey("transparency")] ?: 0.9f
            val backgroundColor = prefs[androidx.datastore.preferences.core.intPreferencesKey("background_color")]
                ?: 0xFF708090.toInt()

            val settingsIntent = Intent(context, DriverWidgetSettingsActivity::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }

            var driver by remember { mutableStateOf<Driver?>(null) }

            LaunchedEffect(driverNumber) {
                if (driverNumber.isNotEmpty()) {
                    driver = repository.getDriverByNumber(driverNumber)
                }
            }

            DriverWidgetContent(
                driver = driver,
                transparency = transparency,
                backgroundColor = backgroundColor,
                onClick = actionStartActivity(settingsIntent)
            )
        }
    }
}

@Composable
fun DriverWidgetContent(
    driver: Driver?,
    transparency: Float,
    backgroundColor: Int,
    onClick: Action
) {
    DriverCard(
        driver = driver,
        transparency = transparency,
        backgroundColor = backgroundColor,
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