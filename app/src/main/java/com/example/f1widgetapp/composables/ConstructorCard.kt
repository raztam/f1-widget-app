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
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.unit.ColorProvider
import com.example.f1widgetapp.R
import com.example.f1widgetapp.data.modals.Constructor

private const val FOOTER_HEIGHT_DP = 34
private const val STRIPE_WIDTH_DP = 10
private const val GROUND_LINE_HEIGHT_DP = 2

@Composable
fun ConstructorCard(
    constructor: Constructor?,
    context: Context = LocalContext.current,
    modifier: GlanceModifier = GlanceModifier,
    transparency: Float = 0.9f,
    backgroundColor: Int = 0xFF708090.toInt()
) {
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
        if (constructor == null) {
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
                    text = "a team",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        } else {
            val teamColor = constructor.teamColorCompose
            val carImage = context.loadTeamImage(constructor.constructorId)
            val (titlePrefix, titleName) = teamTitleLines(
                constructorId = constructor.constructorId,
                name = constructor.name ?: ""
            )
            val footerHeight = FOOTER_HEIGHT_DP.dp
            val stripeWidth = STRIPE_WIDTH_DP.dp

            // Team color stripe — full left edge
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = GlanceModifier
                        .width(stripeWidth)
                        .fillMaxHeight()
                        .background(ColorProvider(teamColor))
                ) {}
            }

            // Team name — top left, clear of the car
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(start = stripeWidth + 8.dp, top = 8.dp, end = 120.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    if (titlePrefix != null) {
                        GlanceText(
                            text = titlePrefix,
                            font = R.font.inter_24pt_regular,
                            fontSize = 12.sp,
                            color = Color.White,
                            letterSpacing = 0.08.sp
                        )
                    }
                    GlanceText(
                        text = titleName,
                        font = R.font.inter_24pt_extrabold,
                        fontSize = 22.sp,
                        color = Color.White,
                        letterSpacing = 0.02.sp
                    )
                }
            }

            // Ground stripe + stats below it
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(modifier = GlanceModifier.fillMaxWidth()) {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .height(GROUND_LINE_HEIGHT_DP.dp)
                            .padding(start = stripeWidth)
                            .background(ColorProvider(teamColor))
                    ) {}

                    Row(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .height(footerHeight)
                            .padding(start = stripeWidth + 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlanceText(
                            text = "P${constructor.position ?: "-"}",
                            font = R.font.inter_24pt_extrabold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        TeamStatFrame(
                            text = "${constructor.score ?: "0"} PTS",
                            teamColor = teamColor,
                            backgroundColor = backgroundColorCompose
                        )
                        Spacer(modifier = GlanceModifier.width(6.dp))
                        TeamStatFrame(
                            text = "${constructor.wins ?: "0"} WINS",
                            teamColor = teamColor,
                            backgroundColor = backgroundColorCompose
                        )
                    }
                }
            }

            // Car last (in front), right side, tires sitting on the ground stripe.
            // Height matches the car aspect ratio (~720x158) so Fit doesn't letterbox and float.
            if (carImage != null) {
                val groundOffset = FOOTER_HEIGHT_DP + GROUND_LINE_HEIGHT_DP - 2
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(start = 72.dp, bottom = groundOffset.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        provider = ImageProvider(carImage),
                        contentDescription = "Team car",
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .height(58.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamStatFrame(
    text: String,
    teamColor: Color,
    backgroundColor: Color
) {
    Box(
        modifier = GlanceModifier
            .cornerRadius(4.dp)
            .background(ColorProvider(teamColor))
            .padding(1.dp)
    ) {
        Box(
            modifier = GlanceModifier
                .cornerRadius(3.dp)
                .background(ColorProvider(backgroundColor))
                .padding(horizontal = 7.dp, vertical = 2.dp)
        ) {
            GlanceText(
                text = text,
                font = R.font.inter_24pt_extrabold,
                fontSize = 11.sp,
                color = Color.White,
                letterSpacing = 0.04.sp
            )
        }
    }
}

private fun teamTitleLines(constructorId: String, name: String): Pair<String?, String> {
    return when (constructorId) {
        "ferrari" -> "SCUDERIA" to "FERRARI"
        "red_bull" -> null to "RED BULL"
        "aston_martin" -> "ASTON" to "MARTIN"
        "rb" -> "RACING" to "BULLS"
        else -> {
            val cleaned = name
                .removeSuffix("F1 Team")
                .removeSuffix("Team")
                .trim()
            val parts = cleaned.split(" ").filter { it.isNotBlank() }
            when {
                parts.size >= 2 ->
                    parts.dropLast(1).joinToString(" ").uppercase() to parts.last().uppercase()
                else -> null to cleaned.uppercase()
            }
        }
    }
}
