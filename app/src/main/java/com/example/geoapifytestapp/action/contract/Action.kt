package com.example.geoapifytestapp.action.contract

import androidx.navigation.NavController

fun interface Action {
    suspend operator fun invoke(navController: NavController)
}