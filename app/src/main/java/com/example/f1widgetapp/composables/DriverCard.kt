package com.example.f1widgetapp.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
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
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.text.FontFamily
import com.example.f1widgetapp.R

@Composable
fun DriverCard(
    driver: Driver?,
) {
    val modifiedDriver = driver?.copy(
        score = "437",
        position = "1"
    )

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(androidx.glance.unit.ColorProvider(Color(0xE6708090)))
            .padding(start = 6.dp, end = 4.dp, top = 8.dp),
        contentAlignment = androidx.glance.layout.Alignment.Center
    ) {
        // Position and score
        Box(
            modifier = GlanceModifier
            .fillMaxSize()
            .padding(end = 12.dp),
            contentAlignment = androidx.glance.layout.Alignment.BottomEnd
        ) {
            Row(
            modifier = GlanceModifier
                .padding(bottom = 0.dp),
            horizontalAlignment = androidx.glance.layout.Alignment.End
            ) {
                GlanceText(
                    text = "P${modifiedDriver?.position ?: "-"}",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = GlanceModifier.width(4.dp))
                GlanceText(
                    text = modifiedDriver?.score ?: "",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 20.sp,
                    color = driver?.teamColorCompose ?: Color.White

                )

            }
        }

        // Driver Image with shadow
        driver?.driverId?.let { driverId ->
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(start = (-4).dp),
                contentAlignment = androidx.glance.layout.Alignment.BottomStart
            ) {

                Image(
                    provider = ImageProvider(R.drawable.max_verstappen),
                    contentDescription = null,
                    modifier = GlanceModifier
                        .width(82.dp)
                        .height(82.dp)
                        .padding(start = 2.dp, top = 2.dp)
                )

                // Main image layer
                Image(
                    provider = ImageProvider(R.drawable.max_verstappen),
                    contentDescription = "Driver photo",
                    modifier = GlanceModifier
                        .width(82.dp)
                        .height(82.dp)
                )
            }
        }

        // Driver name
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(start = 78.dp, bottom = 36.dp),
            horizontalAlignment = androidx.glance.layout.Alignment.Start,
            verticalAlignment = androidx.glance.layout.Alignment.Bottom
        ) {
            GlanceText(
                driver?.givenName ?: "",
                font = R.font.inter_24pt_regular,
                fontSize = 20.sp,
                color = Color.White
            )
            GlanceText(
                text = driver?.familyName ?: "",
                font = R.font.inter_24pt_extrabold,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}
