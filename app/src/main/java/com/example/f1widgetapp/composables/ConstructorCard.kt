package com.example.f1widgetapp.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import com.example.f1widgetapp.R
import com.example.f1widgetapp.data.modals.Constructor

@Composable
fun ConstructorCard(
    constructor: Constructor?,
    modifier: GlanceModifier = GlanceModifier,
    transparency: Float = 0.9f,
    backgroundColor: Int = 0xFF708090.toInt()
) {
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
        contentAlignment = Alignment.TopCenter
    ) {
        if (constructor == null) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlanceText(
                    text = "Click to select",
                    font = R.font.inter_24pt_regular,
                    fontSize = 20.sp,
                    color = Color.White
                )
                GlanceText(
                    text = "a team",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        } else {
            // Team color accent bar on the left
            Row(
                modifier = GlanceModifier.fillMaxSize()
            ) {
                Box(
                    modifier = GlanceModifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(
                            androidx.glance.unit.ColorProvider(
                                constructor.teamColorCompose
                            )
                        )
                ) {}

                Spacer(modifier = GlanceModifier.width(8.dp))

                // Content column
                Column(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Team name
                    GlanceText(
                        text = constructor.name ?: "",
                        font = R.font.inter_24pt_extrabold,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    // Nationality
                    GlanceText(
                        text = constructor.nationality ?: "",
                        font = R.font.inter_24pt_regular,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = GlanceModifier.height(4.dp))

                    // Position and score
                    Row(
                        horizontalAlignment = Alignment.Start
                    ) {
                        GlanceText(
                            text = "P${constructor.position ?: "-"}",
                            font = R.font.inter_24pt_extrabold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = GlanceModifier.width(4.dp))
                        GlanceText(
                            text = constructor.score ?: "",
                            font = R.font.inter_24pt_extrabold,
                            fontSize = 20.sp,
                            color = constructor.teamColorCompose
                        )
                    }
                }
            }
        }
    }
}
