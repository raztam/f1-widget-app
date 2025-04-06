package com.example.f1widgetapp.widgets

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import com.example.f1widgetapp.composables.DriverStandingsTable
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.workers.UpdateWidgetsWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DriverStandingsWidget : GlanceAppWidget(), KoinComponent {
    private val repository: RepositoryInterface by inject()
    
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            DpSize(120.dp, 120.dp),  // Small - 5 drivers
            DpSize(180.dp, 180.dp),  // Medium - 10 drivers
            DpSize(240.dp, 240.dp),  // Large - 15 drivers
            DpSize(300.dp, 300.dp)   // Extra Large - 20 drivers
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Get all drivers, sorted by position
        val drivers = repository.getAllDrivers().sortedBy { 
            it.position?.toIntOrNull() ?: Int.MAX_VALUE 
        }
        
        Log.d("MyDriverStandingsWidget", "Loaded ${drivers.size} drivers")
        
        provideContent {
            DriverStandingsContent(drivers = drivers)
        }
    }
}

@Composable
fun DriverStandingsContent(drivers: List<com.example.f1widgetapp.data.modals.Driver>) {
    Log.d("MyDriverStandingsWidget", "Rendering ${drivers.size} drivers")
    DriverStandingsTable(
        drivers = drivers,
        modifier = GlanceModifier.fillMaxSize(),
        showHeader = false // Hide header row
    )
}

class DriverStandingsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = DriverStandingsWidget
    
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Schedule updates when widget is added to home screen
        UpdateWidgetsWorker.schedule(context)
    }
}