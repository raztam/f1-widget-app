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
            .background(backgroundColorCompose),
        contentAlignment = Alignment.TopStart
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
            // Team color rectangle fixed to the left, ~20% of card width
            val accentWidth = 62.dp

            Box(
                modifier = GlanceModifier
                    .fillMaxHeight()
                    .width(accentWidth)
                    .background(constructor.teamColorCompose)
            ) {}

            // Team name / nationality centered independently from left rectangle
            Column(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.Top
            ) {
                GlanceText(
                    text = constructor.name ?: "",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 20.sp,
                    color = Color.White
                )

                GlanceText(
                    text = constructor.nationality ?: "",
                    font = R.font.inter_24pt_regular,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            // Position and score
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {
                Row(
                    modifier = GlanceModifier.padding(start = 70.dp, bottom = 8.dp),
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
