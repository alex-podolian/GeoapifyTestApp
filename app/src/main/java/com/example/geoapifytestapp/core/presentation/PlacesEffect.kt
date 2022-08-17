package com.example.geoapifytestapp.core.presentation

sealed class PlacesEffect {
    internal data class NavigateToErrorScreen(val data: HashMap<String, Any?>) : PlacesEffect()
}