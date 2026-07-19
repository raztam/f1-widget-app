package com.example.f1widgetapp.composables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.IOException

private const val TAG = "AssetImages"

fun Context.loadDriverImage(driverId: String): Bitmap? =
    loadAssetImage("drivers/$driverId.webp")

fun Context.loadTeamImage(constructorId: String): Bitmap? =
    loadAssetImage("teams/$constructorId.webp")

private fun Context.loadAssetImage(assetPath: String): Bitmap? {
    return try {
        assets.open(assetPath).use { BitmapFactory.decodeStream(it) }
    } catch (e: IOException) {
        Log.w(TAG, "Missing asset image: $assetPath", e)
        null
    }
}
