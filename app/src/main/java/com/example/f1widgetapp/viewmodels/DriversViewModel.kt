package com.example.f1widgetapp.viewmodels

import android.content.Context
import android.util.Log
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

    fun saveWidgetSettings(settings: WidgetSettings, widgetId: Int, context: Context?) {
        repository.saveWidgetSettings(settings, widgetId)

        context?.let { ctx ->
            updateWidget(ctx, widgetId)
        }
    }

    suspend fun getWidgetSettings(widgetId: Int): WidgetSettings {
        return repository.getWidgetSettings(widgetId)
    }

    private fun updateWidget(context: Context, widgetId: Int) {
        viewModelScope.launch {
            val manager = GlanceAppWidgetManager(context)
            val glanceIds = manager.getGlanceIds(DriverWidget::class.java)

            // Find the matching widget ID and update it
            glanceIds.find { manager.getAppWidgetId(it) == widgetId }?.let { glanceId ->
                Log.d("MyDriversViewModel", "Updating widget with ID: $widgetId")
                DriverWidget.update(context, glanceId)
            }
        }
    }
}