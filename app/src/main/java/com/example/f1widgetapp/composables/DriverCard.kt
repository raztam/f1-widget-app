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
    context: Context = LocalContext.current,
    modifier: GlanceModifier = GlanceModifier,
    transparency: Float = 0.9f,
    backgroundColor: Int = 0xFF708090.toInt()
) {
    // Calculate background color with transparency applied.
    // The stored backgroundColor is ARGB; we keep its RGB and apply transparency to alpha.
    val baseColorInt = backgroundColor
    val baseRed = android.graphics.Color.red(baseColorInt)
    val baseGreen = android.graphics.Color.green(baseColorInt)
    val baseBlue = android.graphics.Color.blue(baseColorInt)
    val alpha = (255 * transparency).toInt().coerceIn(0, 255)
    val backgroundColorCompose = Color(
        red = baseRed,
        green = baseGreen,
        blue = baseBlue,
        alpha = alpha
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.glance.unit.ColorProvider(backgroundColorCompose))
            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
        contentAlignment = androidx.glance.layout.Alignment.TopCenter
    ) {
        if (driver == null) {
            // Empty state
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                horizontalAlignment = androidx.glance.layout.Alignment.CenterHorizontally,
                verticalAlignment = androidx.glance.layout.Alignment.CenterVertically
            ) {
                GlanceText(
                    text = "Click to select",
                    font = R.font.inter_24pt_regular,
                    fontSize = 20.sp,
                    color = Color.White
                )
                GlanceText(
                    text = "a driver",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        } else {
            // Get driver image
            val drawableId = try {
                val id = context.getDrawableId(driver.driverId ?: "default_image")
                if (id == 0) null else id
            } catch (e: Exception) {
                null
            }

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
                horizontalAlignment = androidx.glance.layout.Alignment.Start,
                verticalAlignment = androidx.glance.layout.Alignment.Top
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
                modifier = GlanceModifier.fillMaxSize(),
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
}
