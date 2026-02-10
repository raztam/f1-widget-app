package com.example.f1widgetapp.data.modals

data class WidgetSettings(
    val driverNumber: String = "",
    val transparency: Float = 0.9f,
    // ARGB background color. Default matches the original widget background (RGB 112,128,144).
    val backgroundColor: Int = 0xFF708090.toInt()
)
