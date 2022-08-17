package com.example.geoapifytestapp.action

import androidx.navigation.NavController
import com.example.geoapifytestapp.action.contract.Action

class OpenErrorScreenAction(val data: HashMap<*, *>) : Action {
    override suspend fun invoke(navController: NavController) {}
}