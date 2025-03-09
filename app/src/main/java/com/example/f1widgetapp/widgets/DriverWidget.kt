package com.example.f1widgetapp.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import androidx.glance.color.ColorProviders
import com.example.f1widgetapp.composables.DriverCard
import com.example.f1widgetapp.data.api.Api
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.repository.Repository
import com.example.f1widgetapp.data.room.AppDatabase


object DriverWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Initialize database and repository.
        val db = AppDatabase.getDatabase(context)
        val repository = Repository(driverDao = db.driverDao(), remoteDataSource = Api(), context = context)

        // Fetch the selected driver before providing content
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
}