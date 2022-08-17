package com.example.geoapifytestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geoapifytestapp.utils.PlayServicesAvailabilityChecker

class MainViewModelFactory(
    private val availabilityChecker: PlayServicesAvailabilityChecker,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(availabilityChecker) as T
}