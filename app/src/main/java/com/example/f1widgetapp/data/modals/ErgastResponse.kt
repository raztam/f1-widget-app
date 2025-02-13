package com.example.f1widgetapp.data.modals

import com.google.gson.annotations.SerializedName

data class ErgastResponse(
    @SerializedName("MRData")
    val mrData: MRData
)

data class MRData(
    val xmlns: String,
    val series: String,
    val url: String,
    val limit: String,
    val offset: String,
    val total: String,
    @SerializedName("DriverTable")
    val driverTable: DriverTable
)

data class DriverTable(
    val season: String,
    @SerializedName("Drivers")
    val drivers: List<Driver>
)