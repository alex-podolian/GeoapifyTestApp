package com.example.geoapifytestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoapifytestapp.utils.PlayServicesAvailabilityChecker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class MainViewModel (
    availabilityChecker: PlayServicesAvailabilityChecker
) : ViewModel() {

    val uiState = flow {
        emit(
            if (availabilityChecker.isGooglePlayServicesAvailable()) {
                UiState.PlayServicesAvailable
            } else {
                UiState.PlayServicesUnavailable
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Initializing)
}

enum class UiState {
    Initializing, PlayServicesUnavailable, PlayServicesAvailable
}
