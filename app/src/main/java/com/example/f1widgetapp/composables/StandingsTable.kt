package com.example.f1widgetapp.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.f1widgetapp.widgets.StandingRowState

private val RowFontSize = 15.sp
private val PosWidth = 28.dp
private val NameWidth = 120.dp
private val PtsWidth = 44.dp

private val WhiteBold = TextStyle(
    color = ColorProvider(Color.White),
    fontSize = RowFontSize,
    fontWeight = FontWeight.Bold
)
private val WhiteRegular = TextStyle(
    color = ColorProvider(Color.White),
    fontSize = RowFontSize
)
private val LabelStyle = TextStyle(
    color = ColorProvider(Color.White),
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold
)

@Composable
fun StandingsTable(
    title: String,
    rows: List<StandingRowState>,
    modifier: GlanceModifier = GlanceModifier,
    onSettingsClick: Action? = null
) {
    val headerModifier = if (onSettingsClick != null) {
        GlanceModifier.fillMaxWidth().clickable(onSettingsClick)
    } else {
        GlanceModifier.fillMaxWidth()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xE6708090)))
            .padding(8.dp)
    ) {
        Row(
            modifier = headerModifier.padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = LabelStyle)
        }

        Spacer(modifier = GlanceModifier.height(2.dp))

        // One RemoteViews per row via LazyColumn — safe to nest color bar + text per item.
        LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
            itemsIndexed(
                items = rows,
                itemId = { index, row ->
                    ((index + 1L) * 1_000_000L) +
                        (row.position.hashCode().toLong() and 0xFFFFF)
                }
            ) { _, row ->
                StandingRow(
                    row = row,
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
private fun StandingRow(
    row: StandingRowState,
    onClick: Action? = null
) {
    val teamColor = try {
        val androidColor = android.graphics.Color.parseColor(row.teamColor)
        Color(
            android.graphics.Color.red(androidColor),
            android.graphics.Color.green(androidColor),
            android.graphics.Color.blue(androidColor)
        )
    } catch (_: IllegalArgumentException) {
        Color.Black
    }

    val rowModifier = if (onClick != null) {
        GlanceModifier.fillMaxWidth().clickable(onClick)
    } else {
        GlanceModifier.fillMaxWidth()
    }

    Row(
        modifier = rowModifier.padding(horizontal = 4.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = row.position,
            style = WhiteBold,
            modifier = GlanceModifier.width(PosWidth)
        )

        // Team color block
        Box(
            modifier = GlanceModifier
                .width(4.dp)
                .height(14.dp)
                .background(ColorProvider(teamColor))
        ) {}

        Spacer(modifier = GlanceModifier.width(6.dp))

        Text(
            text = row.name,
            style = WhiteRegular,
            maxLines = 1,
            modifier = GlanceModifier.width(NameWidth)
        )

        Text(
            text = row.points,
            style = WhiteBold.copy(textAlign = TextAlign.End),
            modifier = GlanceModifier.width(PtsWidth)
        )
    }
}
