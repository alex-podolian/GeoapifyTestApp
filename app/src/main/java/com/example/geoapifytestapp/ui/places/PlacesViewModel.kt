package com.example.geoapifytestapp.ui.places

import androidx.lifecycle.viewModelScope
import com.example.geoapifytestapp.core.data.api.LocationApi
import com.example.geoapifytestapp.core.presentation.*
import com.example.geoapifytestapp.ui.BaseViewModel

class PlacesViewModel(
    locationApi: LocationApi
) : BaseViewModel<PlacesState, PlacesIntent, PlacesEffect>() {

    override val store: BaseStore<PlacesState, PlacesIntent, PlacesEffect> =
        PlacesStore(viewModelScope)

    init {
        dispatch(PlacesIntent.GetCurrentLocation(locationApi))
    }
}