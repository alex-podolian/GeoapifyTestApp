package com.example.geoapifytestapp.business.usecases

import android.location.Location
import com.example.geoapifytestapp.core.data.api.LocationApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GetCurrentLocationCase {
    suspend operator fun invoke(): Flow<Location?>
}

class DefaultGetCurrentLocationCase(private val locationApi: LocationApi) : GetCurrentLocationCase {
    override suspend fun invoke(): Flow<Location?> = flow {
        emit(locationApi.getCurrentLocation())
    }
}