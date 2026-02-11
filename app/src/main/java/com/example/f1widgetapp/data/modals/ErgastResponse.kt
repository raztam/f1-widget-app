package com.example.f1widgetapp.data.modals

import com.google.gson.annotations.SerializedName

data class ErgastResponse(
    @SerializedName("MRData")
    val mrData: MRData
)

data class MRData(
    val xmlns: String,
    val series: String,
    val url: String? = null,
    val limit: String? = null,
    val offset: String? = null,
    val total: String? = null,
    @SerializedName("DriverTable")
    val driverTable: DriverTable? = null,
    @SerializedName("ConstructorTable")
    val constructorTable: ConstructorTable? = null,
    @SerializedName("StandingsTable")
    val standingsTable: StandingsTable? = null
)

data class DriverTable(
    val season: String,
    @SerializedName("Drivers")
    val drivers: List<Driver>
)

data class StandingsTable(
    val season: String,
    val round: String,
    @SerializedName("StandingsLists")
    val standingsLists: List<StandingsList>
)

data class StandingsList(
    val season: String,
    val round: String,
    @SerializedName("DriverStandings")
    val driverStandings: List<DriverStanding>? = null,
    @SerializedName("ConstructorStandings")
    val constructorStandings: List<ConstructorStanding>? = null
)

data class DriverStanding(
    val position: String,
    val points: String,
    @SerializedName("Driver")
    val driver: DriverReference
)

data class DriverReference(
    val driverId: String
)

data class ConstructorTable(
    val season: String,
    @SerializedName("Constructors")
    val constructors: List<ConstructorReference>
)

data class ConstructorStanding(
    val position: String,
    val points: String,
    @SerializedName("Constructor")
    val constructor: ConstructorReference
)

data class ConstructorReference(
    val constructorId: String,
    val name: String? = null,
    val nationality: String? = null,
    val url: String? = null
)