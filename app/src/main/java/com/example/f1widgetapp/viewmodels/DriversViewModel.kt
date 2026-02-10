package com.example.f1widgetapp.viewmodels

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.WidgetSettings
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.widgets.DriverWidget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DriversViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {
    private val _driversState = MutableStateFlow<List<Driver>>(emptyList())
    val driversState = _driversState.asStateFlow()

    fun fetchDrivers() {
        viewModelScope.launch {
            val fetchedDrivers = repository.getAllDrivers()
            _driversState.value = fetchedDrivers
        }
    }

    suspend fun saveWidgetSettingsAndUpdate(settings: WidgetSettings, widgetId: Int, context: Context?) {
        repository.saveWidgetSettings(settings, widgetId)
        context?.let { ctx ->
            updateWidget(ctx, widgetId)
        }
    }

    suspend fun getWidgetSettings(widgetId: Int): WidgetSettings {
        return repository.getWidgetSettings(widgetId)
    }

    private suspend fun updateWidget(context: Context, widgetId: Int) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(DriverWidget::class.java)

        glanceIds.find { manager.getAppWidgetId(it) == widgetId }?.let { glanceId ->
            val settings = repository.getWidgetSettings(widgetId)

            androidx.glance.appwidget.state.updateAppWidgetState(context, glanceId) { prefs ->
                prefs[androidx.datastore.preferences.core.stringPreferencesKey("driver_number")] = settings.driverNumber
                prefs[androidx.datastore.preferences.core.floatPreferencesKey("transparency")] = settings.transparency
                prefs[androidx.datastore.preferences.core.intPreferencesKey("background_color")] = settings.backgroundColor
                prefs[androidx.datastore.preferences.core.longPreferencesKey("last_update")] = System.currentTimeMillis()
            }

            DriverWidget.update(context, glanceId)
        }
    }
}