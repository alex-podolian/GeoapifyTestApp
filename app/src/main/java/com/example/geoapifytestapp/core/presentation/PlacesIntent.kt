package com.example.geoapifytestapp.core.presentation

import com.example.geoapifytestapp.core.data.api.LocationApi
import com.google.android.gms.maps.model.LatLngBounds

sealed class PlacesIntent {
    data class LoadPlaces(val bounds: LatLngBounds) : PlacesIntent()
    data class GetCurrentLocation(val locationApi: LocationApi) : PlacesIntent()
}