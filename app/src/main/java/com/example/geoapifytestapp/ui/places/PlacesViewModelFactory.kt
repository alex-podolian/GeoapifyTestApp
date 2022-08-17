package com.example.geoapifytestapp.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geoapifytestapp.core.data.api.LocationApi

class PlacesViewModelFactory(
    private val locationApi: LocationApi
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PlacesViewModel( locationApi) as T
}