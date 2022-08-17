package com.example.geoapifytestapp.core.data.api

import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class LocationApi(private val locationProvider: FusedLocationProviderClient) {

    suspend fun getCurrentLocation(): Location? {
        val cancellationTokenSource = CancellationTokenSource()
        return try {
            locationProvider.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).await(cancellationTokenSource)
        } catch (e: SecurityException) {
            Log.w("LocationApi", "Couldn't get location, did you request location permissions?", e)
            null
        }
    }
}