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

data class ConstructorAdditionalInfo(
    val constructorId: String,
    val teamColor: String?
)

class LocalDataSource(private val context: Context) {
    val drivers by lazy {
        Drivers(context)
    }

    val constructors by lazy {
        Constructors(context)
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

    class Constructors(private val context: Context) {
        fun getAdditionalInfo(): Map<String, ConstructorAdditionalInfo> {
            val jsonString = try {
                context.assets.open("constructor_additional_info.json").bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return emptyMap()
            }

            val listType = object : TypeToken<List<ConstructorAdditionalInfo>>() {}.type
            val additionalInfoList: List<ConstructorAdditionalInfo> = Gson().fromJson(jsonString, listType)

            return additionalInfoList.associateBy { it.constructorId }
        }
    }
}