package com.example.f1widgetapp.data.modals

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import androidx.compose.ui.graphics.Color

@Entity(tableName = "drivers")
data class Driver(
    @PrimaryKey
    val driverId: String,
    val givenName: String?,
    val familyName: String?,
    @SerializedName("permanentNumber")
    val driverNumber: String?,
    val score: String?,
    val position: String?,
    val code:String?,
    val teamName:String?,
    val teamColor:String?,
    val nationality:String?
) {
    val fullName: String
        get() = "${givenName ?: ""} ${familyName ?: ""}".trim()

    val teamColorCompose: Color
        get() = try {
            val androidColor = android.graphics.Color.parseColor(teamColor ?: "#000000")
            Color(android.graphics.Color.red(androidColor),
                  android.graphics.Color.green(androidColor),
                  android.graphics.Color.blue(androidColor))
        } catch (e: IllegalArgumentException) {
            Color.Black
        }
}