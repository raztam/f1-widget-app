package com.example.f1widgetapp.composables

import com.example.f1widgetapp.data.modals.Driver
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DriverDropDown(
    drivers: List<Driver>,
    selectedDriver: Driver?,
    onDriverSelected: (Driver) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val buttonText = selectedDriver?.fullName ?: "Select Driver"

    Box(modifier = Modifier.width(200.dp)) {
        Button(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = buttonText, color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(200.dp)
                .heightIn(max = 300.dp)
        ) {
            drivers.forEach { driver ->
                DropdownMenuItem(
                    text = { Text(driver.fullName) },
                    onClick = {
                        onDriverSelected(driver)
                        expanded = false
                    }
                )
            }
        }
    }
}