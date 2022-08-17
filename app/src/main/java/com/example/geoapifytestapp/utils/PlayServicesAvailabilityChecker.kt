package com.example.geoapifytestapp.utils

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayServicesAvailabilityChecker(
    private val context: Context,
    private val googleApiAvailability: GoogleApiAvailability
) {
    suspend fun isGooglePlayServicesAvailable(): Boolean = withContext(Dispatchers.Default) {
        when (googleApiAvailability.isGooglePlayServicesAvailable(context)) {
            ConnectionResult.SUCCESS -> true
            else -> false
        }
    }
}