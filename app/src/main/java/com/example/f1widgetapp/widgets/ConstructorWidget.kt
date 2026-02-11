package com.example.f1widgetapp.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.f1widgetapp.activities.ConstructorWidgetSettingsActivity
import com.example.f1widgetapp.composables.ConstructorCard
import com.example.f1widgetapp.data.modals.Constructor
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.workers.UpdateWidgetsWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConstructorWidget : GlanceAppWidget(), KoinComponent {
    private val repository: RepositoryInterface by inject()

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)

        provideContent {
            val prefs = currentState<Preferences>()
            val constructorId = prefs[stringPreferencesKey("constructor_id")] ?: ""
            val transparency = prefs[floatPreferencesKey("transparency")] ?: 0.9f
            val backgroundColor = prefs[androidx.datastore.preferences.core.intPreferencesKey("background_color")]
                ?: 0xFF708090.toInt()

            val settingsIntent = Intent(context, ConstructorWidgetSettingsActivity::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }

            var constructor by remember { mutableStateOf<Constructor?>(null) }

            LaunchedEffect(constructorId) {
                if (constructorId.isNotEmpty()) {
                    constructor = repository.getConstructorById(constructorId)
                }
            }

            ConstructorWidgetContent(
                constructor = constructor,
                transparency = transparency,
                backgroundColor = backgroundColor,
                onClick = actionStartActivity(settingsIntent)
            )
        }
    }
}

@Composable
fun ConstructorWidgetContent(
    constructor: Constructor?,
    transparency: Float,
    backgroundColor: Int,
    onClick: Action
) {
    ConstructorCard(
        constructor = constructor,
        transparency = transparency,
        backgroundColor = backgroundColor,
        modifier = GlanceModifier.clickable(onClick)
    )
}

class ConstructorWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = ConstructorWidget

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        UpdateWidgetsWorker.schedule(context)
    }
}
