package com.example.f1widgetapp.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.f1widgetapp.data.modals.Driver

@Composable
fun DriverDropDown(
    drivers: List<Driver>,
    selectedDriver: Driver?,
    onDriverSelected: (Driver) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val buttonText = selectedDriver?.fullName ?: "Select"

    Box {
        Button(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = buttonText, color = Color.White)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            drivers.forEach { driver ->
                DropdownMenuItem(
                    onClick = {
                        onDriverSelected(driver)
                        expanded = false
                    },
                    text = {
                        Text(text = driver.fullName)
                    }
                )
            }
        }
    }
}