package com.example.geoapifytestapp.core.data.repository

import com.example.geoapifytestapp.core.data.api.ApiHelper

class PlacesRepository(private val apiHelper: ApiHelper) {
    suspend fun fetchPlaces(category: String, filter: String) = apiHelper.fetchPlaces(category, filter)
}