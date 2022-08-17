package com.example.geoapifytestapp.utils

import android.Manifest.permission
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private val locationPermissions =
    arrayOf(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION)

internal fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

internal fun Activity.shouldShowRationaleFor(permission: String): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

class LocationPermissionState(
    private val activity: ComponentActivity,
    private val onResult: (LocationPermissionState) -> Unit
) {

    var accessCoarseLocationGranted by mutableStateOf(false)
        private set

    var accessCoarseLocationNeedsRationale by mutableStateOf(false)
        private set

    var accessFineLocationGranted by mutableStateOf(false)
        private set

    var accessFineLocationNeedsRationale by mutableStateOf(false)
        private set

    private var permissionRequested by mutableStateOf(false)

    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            permissionRequested = true
            updateState()
            onResult(this)
        }

    init {
        updateState()
    }

    private fun updateState() {
        accessCoarseLocationGranted = activity.hasPermission(permission.ACCESS_COARSE_LOCATION)
        accessCoarseLocationNeedsRationale =
            activity.shouldShowRationaleFor(permission.ACCESS_COARSE_LOCATION)
        accessFineLocationGranted = activity.hasPermission(permission.ACCESS_FINE_LOCATION)
        accessFineLocationNeedsRationale =
            activity.shouldShowRationaleFor(permission.ACCESS_FINE_LOCATION)
    }

    fun requestPermissions() {
        permissionLauncher.launch(locationPermissions)
    }
}