package com.example.f1widgetapp.widgets

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.example.f1widgetapp.data.modals.Constructor
import com.example.f1widgetapp.data.modals.Driver
import com.google.gson.Gson

data class StandingRowState(
    val position: String,
    val name: String,
    val points: String,
    val teamColor: String
)

object StandingsWidgetPrefs {
    val displayCountKey = intPreferencesKey("display_count")
    val lastUpdateKey = longPreferencesKey("last_update")
    val rowsJsonKey = stringPreferencesKey("standing_rows_json")

    private val gson = Gson()

    fun normalizeCount(value: Int?): Int =
        if (value == 10) 10 else 5

    fun rowsFromDrivers(drivers: List<Driver>, count: Int): List<StandingRowState> =
        drivers.take(count).map { driver ->
            StandingRowState(
                position = driver.position ?: "-",
                name = "${driver.givenName?.firstOrNull() ?: ""}. ${driver.familyName ?: ""}",
                points = driver.score ?: "0",
                teamColor = driver.teamColor ?: "#000000"
            )
        }

    fun rowsFromConstructors(constructors: List<Constructor>, count: Int): List<StandingRowState> =
        constructors.take(count).map { constructor ->
            StandingRowState(
                position = constructor.position ?: "-",
                name = constructor.name ?: "-",
                points = constructor.score ?: "0",
                teamColor = constructor.teamColor ?: "#000000"
            )
        }

    fun encodeRows(rows: List<StandingRowState>): String = gson.toJson(rows)

    fun decodeRows(json: String?): List<StandingRowState> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            gson.fromJson(json, Array<StandingRowState>::class.java)?.toList().orEmpty()
        } catch (_: Exception) {
            emptyList()
        }
    }
}

/**
 * Persist display count + visible rows into Glance state, then force a redraw.
 * Rows must live in Glance state so recomposition (without re-running provideGlance)
 * actually changes the list, not just the "Top N" label.
 */
suspend fun <T : GlanceAppWidget> updateStandingsWidget(
    context: Context,
    widgetId: Int,
    displayCount: Int,
    rows: List<StandingRowState>,
    widget: T,
    widgetClass: Class<T>
) {
    val count = StandingsWidgetPrefs.normalizeCount(displayCount)
    val rowsJson = StandingsWidgetPrefs.encodeRows(rows.take(count))
    val manager = GlanceAppWidgetManager(context)
    val glanceIds = manager.getGlanceIds(widgetClass)
    val glanceId = glanceIds.find { manager.getAppWidgetId(it) == widgetId }

    Log.i(
        "StandingsWidget",
        "updateStandingsWidget widgetId=$widgetId displayCount=$count rows=${rows.take(count).size} " +
            "matchedGlance=${glanceId != null} ids=${glanceIds.map { manager.getAppWidgetId(it) }}"
    )

    suspend fun writeState(id: GlanceId) {
        updateAppWidgetState(context, id) { prefs ->
            prefs[StandingsWidgetPrefs.displayCountKey] = count
            prefs[StandingsWidgetPrefs.rowsJsonKey] = rowsJson
            prefs[StandingsWidgetPrefs.lastUpdateKey] = System.currentTimeMillis()
        }
    }

    if (glanceId != null) {
        writeState(glanceId)
        widget.update(context, glanceId)
    } else {
        glanceIds.forEach { writeState(it) }
    }

    widget.updateAll(context)
}
