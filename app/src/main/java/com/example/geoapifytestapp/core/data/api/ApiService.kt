package com.example.geoapifytestapp.core.data.api

import com.example.geoapifytestapp.core.data.model.Places
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("places")
    suspend fun fetchPlaces(
        @Query("categories") category: String,
        @Query("filter") filter: String,
        @Query("limit") limit: String = LIMIT,
        @Query("apiKey") apiKey: String = API_KEY,
    ): Places

    companion object {
        private const val API_KEY = "a53ce63f5f9344ca9f6b8f42cae17a07"
        private const val LIMIT = "20"
    }
}