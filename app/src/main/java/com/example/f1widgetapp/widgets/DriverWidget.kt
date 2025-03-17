package com.example.f1widgetapp.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import com.example.f1widgetapp.composables.DriverCard
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.workers.UpdateWidgetsWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DriverWidget : GlanceAppWidget(), KoinComponent {
    private val repository: RepositoryInterface by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val selectedDriver = repository.getSelectedDriver()

        provideContent {
            DriverWidgetContent(selectedDriver)
        }
    }
}

@Composable
fun DriverWidgetContent(driver: Driver?) {
    DriverCard(driver)
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