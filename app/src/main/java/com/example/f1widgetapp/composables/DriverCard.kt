package com.example.f1widgetapp.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.background
import androidx.glance.layout.padding
import com.example.f1widgetapp.data.modals.Driver


@Composable
fun DriverCard(driver: Driver?) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface) // Uses theme color
            .padding(16.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize()
        ) {
            Text(
                text = "Hello, ${driver?.fullName}!",
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface // Directly use the ColorProvider
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = "Your trip starts soon.",
                style = TextStyle(
                    color = GlanceTheme.colors.secondary // Directly use the ColorProvider
                )
            )
        }
    }
}