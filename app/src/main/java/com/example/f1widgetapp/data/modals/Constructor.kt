package com.example.f1widgetapp.data.modals

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constructors")
data class Constructor(
    @PrimaryKey
    val constructorId: String,
    val name: String?,
    val nationality: String?,
    val url: String? = null,
    val score: String? = null,
    val position: String? = null,
    val teamColor: String? = null
) {
    val teamColorCompose: Color
        get() = try {
            val androidColor = android.graphics.Color.parseColor(teamColor ?: "#000000")
            Color(
                android.graphics.Color.red(androidColor),
                android.graphics.Color.green(androidColor),
                android.graphics.Color.blue(androidColor)
            )
        } catch (e: IllegalArgumentException) {
            Color.Black
        }
}
