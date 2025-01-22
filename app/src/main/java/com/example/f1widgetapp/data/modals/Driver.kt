package com.example.f1widgetapp.data.modals

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "drivers")
data class Driver(

    @SerializedName("driver_number")
    @PrimaryKey
    val driverNumber: Int,
    @SerializedName("full_name")
    val fullName:String,
    @SerializedName("name_acronym")
    val nameAcronym:String,
    @SerializedName("team_name")
    val teamName:String,
    @SerializedName("team_colour")
    val teamColour:String,
    @SerializedName("headshot_url")
    val headshotUrl:String,
    @SerializedName("country_code")
    val countryCode:String,
    @SerializedName("session_key")
    val sessionKey:Int)
