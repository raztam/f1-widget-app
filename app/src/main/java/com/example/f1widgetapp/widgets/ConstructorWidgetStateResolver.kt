package com.example.f1widgetapp.widgets

import com.example.f1widgetapp.data.modals.ConstructorWidgetSettings

data class ResolvedConstructorWidgetState(
    val constructorId: String,
    val transparency: Float,
    val backgroundColor: Int,
    val needsBackfill: Boolean
)

fun resolveConstructorWidgetState(
    glanceConstructorId: String,
    glanceTransparency: Float?,
    glanceBackgroundColor: Int?,
    storedSettings: ConstructorWidgetSettings
): ResolvedConstructorWidgetState {
    val shouldUseStoredSettings = glanceConstructorId.isEmpty() && storedSettings.constructorId.isNotEmpty()

    return if (shouldUseStoredSettings) {
        ResolvedConstructorWidgetState(
            constructorId = storedSettings.constructorId,
            transparency = storedSettings.transparency,
            backgroundColor = storedSettings.backgroundColor,
            needsBackfill = true
        )
    } else {
        ResolvedConstructorWidgetState(
            constructorId = glanceConstructorId,
            transparency = glanceTransparency ?: 0.9f,
            backgroundColor = glanceBackgroundColor ?: 0xFF708090.toInt(),
            needsBackfill = false
        )
    }
}