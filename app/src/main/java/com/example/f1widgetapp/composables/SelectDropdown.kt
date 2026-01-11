package com.example.f1widgetapp.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun <T> SelectDropdown(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    label: String? = null,
    editable: Boolean = false,
    itemToString: (T) -> String = { it.toString() },
    itemContent: @Composable (T, (T) -> Unit) -> Unit = { item, onSelect ->
        // Default behavior: simple DropdownMenuItem that closes on selection
        DropdownMenuItem(
            text = { Text(itemToString(item)) },
            onClick = {
                onSelect(item)
            }
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    // Create a wrapper that closes the dropdown after selection
    val onItemSelectedWithClose: (T) -> Unit = { item ->
        onItemSelected(item)
        expanded = false
    }

    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    // Use a Column with padding to mimic GfG example
    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = selectedItem?.let(itemToString) ?: "",
            onValueChange = { if (editable) { /* optional typing logic */ } },
            label = label?.takeIf { it.isNotEmpty() }?.let { { Text(it) } },
            readOnly = !editable,
            modifier = Modifier
                .fillMaxWidth() // fills Column width (which is padded)
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() }) // same width as TextField
                .heightIn(max = 300.dp)
        ) {
            items.forEach { item ->
                itemContent(item, onItemSelectedWithClose)
            }
        }
    }
}