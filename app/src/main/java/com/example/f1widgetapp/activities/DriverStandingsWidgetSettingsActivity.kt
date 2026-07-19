package com.example.f1widgetapp.activities

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.f1widgetapp.data.modals.DriverStandingsWidgetSettings
import com.example.f1widgetapp.ui.theme.F1WidgetAppTheme
import com.example.f1widgetapp.viewmodels.DriverStandingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

class DriverStandingsWidgetSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val widgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        Log.d("StandingsWidget", "Driver settings opened for widgetId=$widgetId")

        setResult(RESULT_CANCELED, Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        })

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            val viewModel: DriverStandingsViewModel = koinViewModel()
            var displayCount by remember {
                mutableIntStateOf(DriverStandingsWidgetSettings.DEFAULT_DISPLAY_COUNT)
            }
            var loaded by remember { mutableStateOf(false) }

            LaunchedEffect(widgetId) {
                val settings = viewModel.getWidgetSettings(widgetId)
                displayCount = settings.normalizedDisplayCount
                loaded = true
            }

            F1WidgetAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 64.dp, start = 32.dp, end = 32.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "How many drivers to show",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    12.dp,
                                    Alignment.CenterHorizontally
                                )
                            ) {
                                FilterChip(
                                    selected = displayCount == DriverStandingsWidgetSettings.TOP_5,
                                    onClick = {
                                        displayCount = DriverStandingsWidgetSettings.TOP_5
                                    },
                                    label = { Text("Top 5") }
                                )
                                FilterChip(
                                    selected = displayCount == DriverStandingsWidgetSettings.TOP_10,
                                    onClick = {
                                        displayCount = DriverStandingsWidgetSettings.TOP_10
                                    },
                                    label = { Text("Top 10") }
                                )
                            }

                            Button(
                                enabled = loaded,
                                onClick = {
                                    val settings = DriverStandingsWidgetSettings(
                                        displayCount = displayCount
                                    )
                                    Log.d(
                                        "StandingsWidget",
                                        "Saving driver standings widgetId=$widgetId displayCount=$displayCount"
                                    )
                                    lifecycleScope.launch {
                                        withContext(Dispatchers.IO) {
                                            viewModel.saveWidgetSettingsAndUpdate(
                                                settings,
                                                widgetId,
                                                this@DriverStandingsWidgetSettingsActivity
                                            )
                                        }
                                        setResult(RESULT_OK, Intent().apply {
                                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                                        })
                                        finish()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp)
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}
