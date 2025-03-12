package com.example.f1widgetapp.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import com.example.f1widgetapp.data.modals.Driver


@Composable
fun DriverCard(
    driver: Driver?,
) {
    val modifiedDriver = driver?.copy(
        score = "25",
        position = "1"
    )

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(androidx.glance.unit.ColorProvider(Color(0xE6708090)))
            .padding(8.dp)
    ) {
        Column(
            modifier = GlanceModifier
                .padding(start = 16.dp, top = 8.dp)
            ) {
            Text(
                text = modifiedDriver?.givenName ?: "",
                style = TextStyle(
                color = androidx.glance.unit.ColorProvider(Color.White),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = modifiedDriver?.familyName ?: "",
                style = TextStyle(
                color = androidx.glance.unit.ColorProvider(Color.White),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
                )
            )
            }
        }

        // Position and score
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(top = 70.dp), // Fixed padding to position at bottom
            horizontalAlignment = androidx.glance.layout.Alignment.End
        ) {
            Text(
                text = "P${modifiedDriver?.position ?: "-"}",
                style = TextStyle(
                    color = androidx.glance.unit.ColorProvider(Color.White),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.width(4.dp))
            Text(
                text = modifiedDriver?.score ?: "0",
                style = TextStyle(
                    color = androidx.glance.unit.ColorProvider(modifiedDriver?.teamColorCompose ?: Color.White),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
