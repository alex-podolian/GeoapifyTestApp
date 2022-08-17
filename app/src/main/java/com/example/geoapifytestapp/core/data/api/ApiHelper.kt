package com.example.geoapifytestapp.core.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun fetchPlaces(category: String, filter: String) = apiService.fetchPlaces(category, filter)
}