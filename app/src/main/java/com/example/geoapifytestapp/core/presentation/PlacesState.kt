package com.example.geoapifytestapp.core.presentation

import android.location.Location
import com.example.geoapifytestapp.core.data.model.Place

data class PlacesState(
    val isLoading: Boolean = false,
    val location: Location? = null,
    val places: List<Place>? = null
)