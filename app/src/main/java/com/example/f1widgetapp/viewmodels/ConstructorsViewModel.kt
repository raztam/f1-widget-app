package com.example.f1widgetapp.viewmodels

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1widgetapp.data.modals.Constructor
import com.example.f1widgetapp.data.modals.ConstructorWidgetSettings
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.widgets.ConstructorWidget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConstructorsViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {
    private val _constructorsState = MutableStateFlow<List<Constructor>>(emptyList())
    val constructorsState = _constructorsState.asStateFlow()

    fun fetchConstructors() {
        viewModelScope.launch {
            val fetchedConstructors = repository.getAllConstructors()
            _constructorsState.value = fetchedConstructors
        }
    }

    suspend fun saveWidgetSettingsAndUpdate(settings: ConstructorWidgetSettings, widgetId: Int, context: Context?) {
        repository.saveConstructorWidgetSettings(settings, widgetId)
        context?.let { ctx ->
            updateWidget(ctx, widgetId)
        }
    }

    suspend fun getWidgetSettings(widgetId: Int): ConstructorWidgetSettings {
        return repository.getConstructorWidgetSettings(widgetId)
    }

    private suspend fun updateWidget(context: Context, widgetId: Int) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(ConstructorWidget::class.java)

        glanceIds.find { manager.getAppWidgetId(it) == widgetId }?.let { glanceId ->
            val settings = repository.getConstructorWidgetSettings(widgetId)

            androidx.glance.appwidget.state.updateAppWidgetState(context, glanceId) { prefs ->
                prefs[androidx.datastore.preferences.core.stringPreferencesKey("constructor_id")] = settings.constructorId
                prefs[androidx.datastore.preferences.core.floatPreferencesKey("transparency")] = settings.transparency
                prefs[androidx.datastore.preferences.core.intPreferencesKey("background_color")] = settings.backgroundColor
                prefs[androidx.datastore.preferences.core.longPreferencesKey("last_update")] = System.currentTimeMillis()
            }

            ConstructorWidget.update(context, glanceId)
        }
    }
}
