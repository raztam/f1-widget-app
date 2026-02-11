package com.example.f1widgetapp.activities

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.f1widgetapp.data.modals.Constructor
import com.example.f1widgetapp.data.modals.ConstructorWidgetSettings
import com.example.f1widgetapp.ui.theme.F1WidgetAppTheme
import com.example.f1widgetapp.composables.SelectDropdown
import com.example.f1widgetapp.viewmodels.ConstructorsViewModel
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class ConstructorWidgetSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val widgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        Log.d("ConstructorWidgetSettings", "Widget ID: $widgetId")

        setResult(RESULT_CANCELED, Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        })

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            val constructorsViewModel: ConstructorsViewModel = koinViewModel()
            val constructors = constructorsViewModel.constructorsState.collectAsState()
            var selectedConstructor by remember { mutableStateOf<Constructor?>(null) }
            var transparency by remember { mutableFloatStateOf(0.9f) }
            var backgroundColorInt by remember { mutableStateOf(0xFF708090.toInt()) }
            val colorPickerController = rememberColorPickerController()

            LaunchedEffect(Unit) {
                constructorsViewModel.fetchConstructors()
            }

            LaunchedEffect(constructors.value.isNotEmpty()) {
                if (constructors.value.isNotEmpty()) {
                    val settings = constructorsViewModel.getWidgetSettings(widgetId)
                    transparency = settings.transparency
                    backgroundColorInt = settings.backgroundColor

                    if (settings.constructorId.isNotEmpty()) {
                        selectedConstructor =
                            constructors.value.find { it.constructorId == settings.constructorId }
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
                                    text = "Choose a team to display in your widget",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                SelectDropdown(
                                    items = constructors.value,
                                    selectedItem = selectedConstructor,
                                    onItemSelected = { constructor: Constructor ->
                                        selectedConstructor = constructor
                                    },
                                    itemToString = { it.name ?: "" }
                                )

                                Text(
                                    text = "Background color",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(backgroundColorInt), CircleShape)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                            shape = CircleShape
                                        )
                                )

                                HsvColorPicker(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                        .height(180.dp),
                                    controller = colorPickerController,
                                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                                        backgroundColorInt = colorEnvelope.color.toArgb()
                                    }
                                )

                                BrightnessSlider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    controller = colorPickerController
                                )

                                Text(
                                    text = "Transparency: ${(transparency * 100).toInt()}%",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
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
                                        val constructorId = selectedConstructor?.constructorId ?: ""
                                        val settings = ConstructorWidgetSettings(
                                            constructorId = constructorId,
                                            transparency = transparency,
                                            backgroundColor = backgroundColorInt
                                        )

                                        lifecycleScope.launch {
                                            constructorsViewModel.saveWidgetSettingsAndUpdate(
                                                settings,
                                                widgetId,
                                                this@ConstructorWidgetSettingsActivity
                                            )

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
}
