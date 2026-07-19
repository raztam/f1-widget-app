package com.example.f1widgetapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.f1widgetapp.data.modals.ConstructorStandingsWidgetSettings
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.widgets.ConstructorStandingsWidget
import com.example.f1widgetapp.widgets.StandingsWidgetPrefs
import com.example.f1widgetapp.widgets.updateStandingsWidget

class ConstructorStandingsViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {

    suspend fun getWidgetSettings(widgetId: Int): ConstructorStandingsWidgetSettings {
        return repository.getConstructorStandingsWidgetSettings(widgetId)
    }

    suspend fun saveWidgetSettingsAndUpdate(
        settings: ConstructorStandingsWidgetSettings,
        widgetId: Int,
        context: Context?
    ) {
        val normalized = settings.copy(displayCount = settings.normalizedDisplayCount)
        repository.saveConstructorStandingsWidgetSettings(normalized, widgetId)

        val count = normalized.normalizedDisplayCount
        val constructors = repository.getAllConstructors().sortedBy {
            it.position?.toIntOrNull() ?: Int.MAX_VALUE
        }
        val rows = StandingsWidgetPrefs.rowsFromConstructors(constructors, count)

        context?.let { ctx ->
            updateStandingsWidget(
                context = ctx.applicationContext,
                widgetId = widgetId,
                displayCount = count,
                rows = rows,
                widget = ConstructorStandingsWidget,
                widgetClass = ConstructorStandingsWidget::class.java
            )
        }
    }
}
