package com.example.f1widgetapp.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.key
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.f1widgetapp.activities.ConstructorStandingsWidgetSettingsActivity
import com.example.f1widgetapp.composables.StandingsTable
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.workers.UpdateWidgetsWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConstructorStandingsWidget : GlanceAppWidget(), KoinComponent {
    private val repository: RepositoryInterface by inject()

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val storedCount = repository
            .getConstructorStandingsWidgetSettings(widgetId)
            .normalizedDisplayCount

        val constructors = repository.getAllConstructors().sortedBy {
            it.position?.toIntOrNull() ?: Int.MAX_VALUE
        }
        val rows = StandingsWidgetPrefs.rowsFromConstructors(constructors, storedCount)

        updateAppWidgetState(context, id) { prefs ->
            prefs[StandingsWidgetPrefs.displayCountKey] = storedCount
            prefs[StandingsWidgetPrefs.rowsJsonKey] = StandingsWidgetPrefs.encodeRows(rows)
            prefs[StandingsWidgetPrefs.lastUpdateKey] = System.currentTimeMillis()
        }

        Log.i(
            "StandingsWidget",
            "ConstructorStandings provideGlance widgetId=$widgetId storedCount=$storedCount " +
                "dbConstructors=${constructors.size} rows=${rows.size}"
        )

        provideContent {
            val prefs = currentState<Preferences>()
            val displayCount = StandingsWidgetPrefs.normalizeCount(
                prefs[StandingsWidgetPrefs.displayCountKey]
            )
            val lastUpdate = prefs[StandingsWidgetPrefs.lastUpdateKey] ?: 0L
            val standingRows = StandingsWidgetPrefs.decodeRows(
                prefs[StandingsWidgetPrefs.rowsJsonKey]
            ).ifEmpty { rows }

            val settingsIntent =
                Intent(context, ConstructorStandingsWidgetSettingsActivity::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
            val openSettings = actionStartActivity(settingsIntent)

            key(displayCount, lastUpdate, standingRows.size) {
                StandingsTable(
                    title = "Top $displayCount",
                    rows = standingRows,
                    modifier = GlanceModifier.fillMaxSize(),
                    onSettingsClick = openSettings
                )
            }
        }
    }
}

class ConstructorStandingsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = ConstructorStandingsWidget

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        UpdateWidgetsWorker.schedule(context)
    }
}
