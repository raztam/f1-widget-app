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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.modals.WidgetSettings
import com.example.f1widgetapp.ui.theme.F1WidgetAppTheme
import com.example.f1widgetapp.composables.SelectDropdown
import com.example.f1widgetapp.viewmodels.DriversViewModel
import org.koin.androidx.compose.koinViewModel

class DriverWidgetSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get the widget ID from the intent extras
        val widgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        Log.d("MyDriverWidgetSettingsActivity", "Widget ID: $widgetId")

        // Set result as canceled when the user cancels the activity
        setResult(RESULT_CANCELED, Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        })

        // Exit if widget ID is invalid
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            val driversViewModel: DriversViewModel = koinViewModel()
            val drivers = driversViewModel.driversState.collectAsState()
            var selectedDriver by remember { mutableStateOf<Driver?>(null) }
            var transparency by remember { mutableFloatStateOf(0.9f) }

            LaunchedEffect(Unit) { // run once
                driversViewModel.fetchDrivers()
            }

            // Load settings and update selected driver when drivers are loaded
            LaunchedEffect(drivers.value.isNotEmpty()) {
                if (drivers.value.isNotEmpty()) {
                    val settings = driversViewModel.getWidgetSettings(widgetId)
                    transparency = settings.transparency

                    // Load driver from settings
                    if (settings.driverNumber.isNotEmpty()) {
                        selectedDriver =
                            drivers.value.find { it.driverNumber == settings.driverNumber }
                    }
                }
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
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 64.dp, start = 32.dp, end = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Choose a driver to display in your widget",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                SelectDropdown(
                                    items = drivers.value,
                                    selectedItem = selectedDriver,
                                    onItemSelected = { driver: Driver ->
                                        selectedDriver = driver
                                    },
                                    itemToString = { it.fullName }
                                )

                                Text(
                                    text = "Transparency: ${(transparency * 100).toInt()}%",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
                                )
                                Slider(
                                    value = transparency,
                                    onValueChange = { transparency = it },
                                    valueRange = 0f..1f,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                )

                                Button(
                                    onClick = {
                                        val driverNumber = selectedDriver?.driverNumber ?: ""
                                        val settings = WidgetSettings(
                                            driverNumber = driverNumber,
                                            transparency = transparency
                                        )
                                        driversViewModel.saveWidgetSettings(
                                            settings,
                                            widgetId,
                                            this@DriverWidgetSettingsActivity
                                        )
                                        // Set result as OK, then finish the activity
                                        setResult(RESULT_OK, Intent().apply {
                                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                                        })
                                        finish()
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

}
