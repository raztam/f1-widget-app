package com.example.f1widgetapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

data class DriverAdditionalInfo(
    val driverId: String,
    val teamName: String?,
    val teamColor: String?,
    val imageUrl: String?
)

class LocalDataSource(private val context: Context) {
    val drivers by lazy {
        Drivers(context)
    }

    class Drivers(private val context: Context) {
        fun getAdditionalInfo(): Map<String, DriverAdditionalInfo> {
            val jsonString = try {
                context.assets.open("driver_additional_info.json").bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return emptyMap()
            }

            val listType = object : TypeToken<List<DriverAdditionalInfo>>() {}.type
            val additionalInfoList: List<DriverAdditionalInfo> = Gson().fromJson(jsonString, listType)

            return additionalInfoList.associateBy { it.driverId }
        }
    }
}