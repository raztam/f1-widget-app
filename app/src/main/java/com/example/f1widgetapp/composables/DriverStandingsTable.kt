package com.example.f1widgetapp.composables

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
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
import com.example.f1widgetapp.R
import com.example.f1widgetapp.data.modals.Driver

@Composable
fun DriverStandingsTable(
    drivers: List<Driver>,
    modifier: GlanceModifier = GlanceModifier,
    context: Context = LocalContext.current,
    showHeader: Boolean = true
) {
    // Get the current size of the widget to determine how many drivers to show
    val size = LocalSize.current
    val maxDrivers = 20

    Log.d("MyDriverStandingsTable", "Widget size: ${size.width}x${size.height}")
    Log.d("MyDriverStandingsTable", "Drivers: ${drivers.joinToString { it.familyName ?: "" }}")
    
    // Get the subset of drivers to display based on widget size
    val driversToShow = drivers.take(maxDrivers)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.glance.unit.ColorProvider(Color(0xE6708090)))
            .padding(8.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize()
        ) {
            // Header row - only show if showHeader is true
            if (showHeader) {
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(androidx.glance.unit.ColorProvider(Color(0xFF4A5568)))
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Position column
                    GlanceText(
                        text = "Pos",
                        font = R.font.inter_24pt_extrabold,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = GlanceModifier.width(36.dp)
                    )
                    
                    // Driver name column
                    GlanceText(
                        text = "Driver",
                        font = R.font.inter_24pt_extrabold,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = GlanceModifier.width(100.dp)
                    )
                    
                    // Points column
                    GlanceText(
                        text = "Pts",
                        font = R.font.inter_24pt_extrabold,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = GlanceModifier.width(50.dp)
                    )
                }
                
                Spacer(modifier = GlanceModifier.height(4.dp))
            }
            
            // Driver rows
            driversToShow.forEach { driver ->
                DriverRow(driver = driver)
                Spacer(modifier = GlanceModifier.height(2.dp))
            }
            
            // Show message if there are more drivers available
            if (drivers.size > driversToShow.size) {
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlanceText(
                        text = "Resize widget to see more drivers",
                        font = R.font.inter_24pt_regular,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun DriverRow(
    driver: Driver,
    modifier: GlanceModifier = GlanceModifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Position
        Box(
            modifier = GlanceModifier.width(36.dp),
            contentAlignment = Alignment.CenterStart,
            content = {
                GlanceText(
                    text = driver.position ?: "-",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        )
        
        // Driver name with team color indicator
        Row(
            modifier = GlanceModifier.width(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Team color indicator
            Box(
                modifier = GlanceModifier
                    .width(4.dp)
                    .height(14.dp)
                    .background(androidx.glance.unit.ColorProvider(driver.teamColorCompose))
            ) {
                // Empty content, but needed for Glance Box composable
            }
            
            Spacer(modifier = GlanceModifier.width(4.dp))
            
            // Driver name
            GlanceText(
                text = "${driver.givenName?.first() ?: ""}. ${driver.familyName ?: ""}",
                font = R.font.inter_24pt_regular,
                fontSize = 14.sp,
                color = Color.White
            )
        }
        
        // Points
        Box(
            modifier = GlanceModifier.width(50.dp),
            contentAlignment = Alignment.CenterEnd,
            content = {
                GlanceText(
                    text = driver.score ?: "0",
                    font = R.font.inter_24pt_extrabold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        )
    }
}