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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.ui.theme.F1WidgetAppTheme
import com.example.f1widgetapp.composables.DriverDropDown
import com.example.f1widgetapp.viewmodels.DriversViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
            val selectedDriver = remember { mutableStateOf<Driver?>(null) }

            LaunchedEffect(Unit) { // run once
                driversViewModel.fetchDrivers()
                selectedDriver.value = driversViewModel.getDriverForWidget(widgetId)
            }

            F1WidgetAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Select driver for widget",
                            modifier = Modifier.padding(16.dp)
                        )

                        DriverDropDown(
                            drivers = drivers.value,
                            selectedDriver = selectedDriver.value,
                            onDriverSelected = { driver: Driver ->
                                selectedDriver.value = driver
                                driversViewModel.saveDriverForWidget(
                                    driver,
                                    widgetId,
                                    this@DriverWidgetSettingsActivity
                                )


                                // Set result as OK,then finish the activity
                                setResult(RESULT_OK, Intent().apply {
                                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                                })
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}
