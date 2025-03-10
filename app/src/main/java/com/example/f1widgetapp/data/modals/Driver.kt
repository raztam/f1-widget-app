package com.example.f1widgetapp.data.modals

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "drivers")
data class Driver(
    @PrimaryKey
    val driverId: String,
    val givenName: String?,
    val familyName: String?,
    val imageUrl: String?,
    @SerializedName("permanentNumber")
    val driverNumber: String,
    val score: String?,
    val position: String?,
    val code:String?,
    val teamName:String?,
    val teamColor:String?,
    val nationality:String?
) {
    val fullName: String
        get() = "${givenName ?: ""} ${familyName ?: ""}".trim()
}