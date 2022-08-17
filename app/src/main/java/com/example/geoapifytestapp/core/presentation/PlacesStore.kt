package com.example.geoapifytestapp.core.presentation

import com.example.geoapifytestapp.KEY_ERROR_TEXT
import com.example.geoapifytestapp.business.usecases.DefaultFetchPlacesCase
import com.example.geoapifytestapp.business.usecases.DefaultGetCurrentLocationCase
import com.example.geoapifytestapp.core.data.api.ApiHelper
import com.example.geoapifytestapp.core.data.api.LocationApi
import com.example.geoapifytestapp.core.data.api.NetworkManager
import com.example.geoapifytestapp.core.data.repository.PlacesRepository
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

class PlacesStore(private val scope: CoroutineScope) :
    BaseStore<PlacesState, PlacesIntent, PlacesEffect>(scope, PlacesState()) {

    override suspend fun processIntent(
        state: PlacesState,
        intent: PlacesIntent,
        statePublisher: StatePublisher<PlacesState>,
        effectPublisher: EffectPublisher<PlacesEffect>
    ): Unit = when (intent) {
        is PlacesIntent.LoadPlaces -> fetchPlaces(
            intent.bounds, state, statePublisher, effectPublisher
        )
        is PlacesIntent.GetCurrentLocation -> getCurrentLocation(
            state, statePublisher, effectPublisher, intent.locationApi
        )
    }

    private suspend fun getCurrentLocation(
        state: PlacesState,
        statePublisher: StatePublisher<PlacesState>,
        effectPublisher: EffectPublisher<PlacesEffect>,
        locationApi: LocationApi
    ) {
        withContext(Dispatchers.IO) {
            DefaultGetCurrentLocationCase(locationApi)
                .invoke()
                .onStart { statePublisher(state.copy(isLoading = true)) }
                .catch {
                    val data = HashMap<String, Any?>()
                    data.apply {
                        put(KEY_ERROR_TEXT, it.message)
                    }
                    effectPublisher(PlacesEffect.NavigateToErrorScreen(data))
                    statePublisher(state.copy(isLoading = false))
                    it.printStackTrace()
                }
                .collect {
                    statePublisher(state.copy(isLoading = false, location = it))
                }
        }
    }

    private suspend fun fetchPlaces(
        bounds: LatLngBounds,
        state: PlacesState,
        statePublisher: StatePublisher<PlacesState>,
        effectPublisher: EffectPublisher<PlacesEffect>
    ) {
        val southWest = bounds.southwest
        val northEast = bounds.northeast
        val filter =
            "rect:${southWest.longitude},${southWest.latitude},${northEast.longitude},${northEast.latitude}"
        withContext(Dispatchers.IO) {
            DefaultFetchPlacesCase(PlacesRepository(ApiHelper(NetworkManager.apiService)))
                .invoke(filter = filter)
                .onStart { statePublisher(state.copy(isLoading = true)) }
                .catch {
                    val data = HashMap<String, Any?>()
                    data.apply {
                        put(KEY_ERROR_TEXT, it.message)
                    }
                    effectPublisher(PlacesEffect.NavigateToErrorScreen(data))
                    statePublisher(state.copy(isLoading = false))
                    it.printStackTrace()
                }
                .collect {
                    statePublisher(state.copy(isLoading = false, places = it))
                }
        }
    }
}