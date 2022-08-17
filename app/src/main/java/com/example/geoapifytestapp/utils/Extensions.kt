package com.example.geoapifytestapp.utils

import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.Task
import kotlin.math.cos

fun Context.isGPSEnabled(): Boolean {
    val locationManager: LocationManager? =
        ContextCompat.getSystemService(this, LocationManager::class.java)
    return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
}

fun Context.turnOnGPS(resultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
    val locationRequest = LocationRequest.create();
    locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY;
    locationRequest.interval = 5000;
    locationRequest.fastestInterval = 2000;
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    builder.setAlwaysShow(true)
    val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(this)
        .checkLocationSettings(builder.build())
    result.addOnCompleteListener { task ->
        try {
            task.getResult(ApiException::class.java)
            Toast.makeText(this, "GPS is already tured on", Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            when (e.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    val resolvableApiException = e as ResolvableApiException
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(resolvableApiException.resolution).build()
                    resultLauncher.launch(intentSenderRequest)
                } catch (ex: IntentSender.SendIntentException) {
                    ex.printStackTrace()
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
            }
        }
    }
}

fun Location.getBounds(): LatLngBounds {
    val latRadian = Math.toRadians(this.latitude)

    val degLatKm = 110.574235
    val degLongKm = 110.572833 * cos(latRadian)
    val deltaLat = 5000 / 1000.0 / degLatKm
    val deltaLong = 5000 / 1000.0 / degLongKm

    val minLat = this.latitude - deltaLat
    val minLong = this.longitude - deltaLong
    val maxLat = this.latitude + deltaLat
    val maxLong = this.longitude + deltaLong
    return LatLngBounds(LatLng(minLat, minLong), LatLng(maxLat, maxLong))
}