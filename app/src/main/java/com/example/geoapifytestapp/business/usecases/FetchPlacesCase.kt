package com.example.geoapifytestapp.business.usecases

import com.example.geoapifytestapp.core.data.model.Place
import com.example.geoapifytestapp.core.data.model.Places
import com.example.geoapifytestapp.core.data.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

interface FetchPlacesCase {
    suspend operator fun invoke(filter: String): Flow<MutableList<Place>>
}

class DefaultFetchPlacesCase(private val placesRepository: PlacesRepository) : FetchPlacesCase {
    override suspend fun invoke(filter: String): Flow<MutableList<Place>> {
        val foodAndDrinkFlow =
            flow { emit(placesRepository.fetchPlaces("commercial.food_and_drink", filter)) }
        val supermarketsFlow =
            flow { emit(placesRepository.fetchPlaces("commercial.supermarket", filter)) }
        val combinedPlaces =
            foodAndDrinkFlow.combine(supermarketsFlow) { foodAndDrink: Places, supermarkets: Places ->
                mutableListOf<Place>().apply {
                    addAll(foodAndDrink.places)
                    addAll(supermarkets.places)
                }
            }.distinctUntilChanged()
        return combinedPlaces
    }
}