package com.example.f1widgetapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.f1widgetapp.data.modals.DriverStandingsWidgetSettings
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.widgets.DriverStandingsWidget
import com.example.f1widgetapp.widgets.StandingsWidgetPrefs
import com.example.f1widgetapp.widgets.updateStandingsWidget

class DriverStandingsViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {

    suspend fun getWidgetSettings(widgetId: Int): DriverStandingsWidgetSettings {
        return repository.getDriverStandingsWidgetSettings(widgetId)
    }

    suspend fun saveWidgetSettingsAndUpdate(
        settings: DriverStandingsWidgetSettings,
        widgetId: Int,
        context: Context?
    ) {
        val normalized = settings.copy(displayCount = settings.normalizedDisplayCount)
        repository.saveDriverStandingsWidgetSettings(normalized, widgetId)

        val count = normalized.normalizedDisplayCount
        val drivers = repository.getAllDrivers().sortedBy {
            it.position?.toIntOrNull() ?: Int.MAX_VALUE
        }
        val rows = StandingsWidgetPrefs.rowsFromDrivers(drivers, count)

        context?.let { ctx ->
            updateStandingsWidget(
                context = ctx.applicationContext,
                widgetId = widgetId,
                displayCount = count,
                rows = rows,
                widget = DriverStandingsWidget,
                widgetClass = DriverStandingsWidget::class.java
            )
        }
    }
}
