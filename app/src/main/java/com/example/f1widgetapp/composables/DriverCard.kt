package com.example.f1widgetapp.composables

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.unit.ColorProvider
import com.example.f1widgetapp.R
import com.example.f1widgetapp.data.modals.Driver

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
    // Keep RGB from the stored ARGB color and apply the transparency slider to alpha.
    val baseRed = android.graphics.Color.red(backgroundColor)
    val baseGreen = android.graphics.Color.green(backgroundColor)
    val baseBlue = android.graphics.Color.blue(backgroundColor)
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
            .background(ColorProvider(backgroundColorCompose))
    ) {
        if (driver == null) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlanceText(
                    text = "Click to select",
                    font = R.font.inter_24pt_regular,
                    fontSize = 18.sp,
                    color = Color.White
                )
                GlanceText(
                    text = "a driver",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        } else {
            val teamColor = driver.teamColorCompose
            val drawableId = try {
                val id = context.getDrawableId(driver.driverId)
                if (id == 0) null else id
            } catch (_: Exception) {
                null
            }

            // Team accent stripe — flush left
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = GlanceModifier
                        .width(10.dp)
                        .fillMaxHeight()
                        .background(ColorProvider(teamColor))
                ) {}
            }

            // Driver portrait — left, overlapping the bottom bar
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(start = 10.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                if (drawableId != null) {
                    Image(
                        provider = ImageProvider(drawableId),
                        contentDescription = "Driver photo",
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier
                            .width(110.dp)
                            .fillMaxHeight()
                    )
                }
            }

            // Driver name — upper area to the right of the portrait
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(start = 118.dp, top = 8.dp, end = 12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    GlanceText(
                        text = (driver.givenName ?: "").uppercase(),
                        font = R.font.inter_24pt_regular,
                        fontSize = 13.sp,
                        color = Color.White,
                        letterSpacing = 0.06.sp
                    )
                    GlanceText(
                        text = (driver.familyName ?: "").uppercase(),
                        font = R.font.inter_24pt_extrabold,
                        fontSize = 22.sp,
                        color = Color.White,
                        letterSpacing = 0.02.sp
                    )
                }
            }

            // Stats row — bottom right (no black bar for now)
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(
                    modifier = GlanceModifier.padding(end = 8.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlanceText(
                        text = "P${driver.position ?: "-"}",
                        font = R.font.inter_24pt_extrabold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Spacer(modifier = GlanceModifier.width(6.dp))
                    // Points: thin team-color frame
                    Box(
                        modifier = GlanceModifier
                            .cornerRadius(4.dp)
                            .background(ColorProvider(teamColor))
                            .padding(1.dp)
                    ) {
                        Box(
                            modifier = GlanceModifier
                                .cornerRadius(3.dp)
                                .background(ColorProvider(backgroundColorCompose))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            GlanceText(
                                text = "${driver.score ?: "0"} PTS",
                                font = R.font.inter_24pt_extrabold,
                                fontSize = 11.sp,
                                color = Color.White,
                                letterSpacing = 0.04.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
