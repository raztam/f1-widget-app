package com.example.f1widgetapp.data.modals

data class DriverStandingsWidgetSettings(
    val displayCount: Int = DEFAULT_DISPLAY_COUNT
) {
    val normalizedDisplayCount: Int
        get() = if (displayCount == TOP_10) TOP_10 else TOP_5

    companion object {
        const val TOP_5 = 5
        const val TOP_10 = 10
        const val DEFAULT_DISPLAY_COUNT = TOP_5
    }
}
