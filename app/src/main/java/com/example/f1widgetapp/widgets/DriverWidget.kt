package com.example.f1widgetapp.widgets

import android.content.Context
import androidx.compose.runtime.Composable
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


object DriverWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            DriverWidgetContent()
        }
    }
}


@Composable
fun DriverWidgetContent() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface) // Uses theme color instead
            .padding(16.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello, Driver!",
                style = androidx.glance.text.TextStyle(
                    color = GlanceTheme.colors.onSurface // Uses theme-based text color
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = "Your trip starts soon.",
                style = androidx.glance.text.TextStyle(
                    color = GlanceTheme.colors.secondary // Another theme-based color
                )
            )
        }
    }
}
class DriverWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = DriverWidget
}