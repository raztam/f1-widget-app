package com.example.f1widgetapp.composables

import android.content.Context
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
import androidx.glance.LocalContext
import androidx.glance.text.FontFamily
import com.example.f1widgetapp.R

fun Context.getDrawableId(imageName: String): Int {
    return resources.getIdentifier(imageName, "drawable", packageName)
}

@Composable
fun DriverCard(
    driver: Driver?,
    context: Context = LocalContext.current
) {

    // Get driver image
    val drawableId = try {
     val id = context.getDrawableId(driver?.driverId ?: "default_image")
        if (id == 0) null else id
    } catch (e: Exception) {
        null
    }

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(androidx.glance.unit.ColorProvider(Color(0xE6708090)))
            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
        contentAlignment = androidx.glance.layout.Alignment.TopCenter  // Changed to TopCenter
    ) {
        // Driver Image with shadow - on the right
        if (drawableId != null) {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(end = 12.dp),
                contentAlignment = androidx.glance.layout.Alignment.BottomEnd
            ) {
                Image(
                    provider = ImageProvider(drawableId),
                    contentDescription = null,
                    modifier = GlanceModifier
                        .width(82.dp)
                        .height(82.dp)
                )

                // Main image layer
                Image(
                    provider = ImageProvider(drawableId),
                    contentDescription = "Driver photo",
                    modifier = GlanceModifier
                        .width(82.dp)
                        .height(82.dp)
                )
            }
        }

        // Driver name - centered
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 65.dp),
            horizontalAlignment = androidx.glance.layout.Alignment.Start,  // Changed to Start alignment
            verticalAlignment = androidx.glance.layout.Alignment.Top  // Changed to Top alignment
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

        // Position and score
        Box(
            modifier = GlanceModifier
                .fillMaxSize(),
            contentAlignment = androidx.glance.layout.Alignment.BottomStart
        ) {
            Row(
                modifier = GlanceModifier
                    .padding(start = 4.dp),
                horizontalAlignment = androidx.glance.layout.Alignment.Start
            ) {
                GlanceText(
                    text = "P${driver?.position ?: "-"}",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = GlanceModifier.width(4.dp))
                GlanceText(
                    text = driver?.score ?: "",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 20.sp,
                    color = driver?.teamColorCompose ?: Color.White
                )
            }
        }
    }
}
