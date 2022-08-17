package com.example.geoapifytestapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoapifytestapp.action.OpenErrorScreenAction
import com.example.geoapifytestapp.action.RetryAction
import com.example.geoapifytestapp.action.contract.ActionExecutor
import com.example.geoapifytestapp.ui.*
import com.example.geoapifytestapp.ui.places.PlacesScreen
import com.example.geoapifytestapp.ui.theme.Blue700
import com.example.geoapifytestapp.ui.theme.GeoapifyTestAppTheme
import com.example.geoapifytestapp.utils.LocationPermissionState
import com.example.geoapifytestapp.utils.PlayServicesAvailabilityChecker
import com.example.geoapifytestapp.utils.isGPSEnabled
import com.example.geoapifytestapp.utils.turnOnGPS
import com.google.android.gms.common.GoogleApiAvailability
import java.io.Serializable

private const val NAV_ROUTE_INITIALIZING_SCREEN = "InitializingScreen"
private const val NAV_ROUTE_SERVICE_UNAVAILABLE_SCREEN = "ServiceUnavailableScreen"
private const val NAV_ROUTE_GPS_DISABLED_SCREEN = "GpsDisabledScreen"
private const val NAV_ROUTE_NO_PERMISSIONS_SCREEN = "NoPermissionScreen"
private const val NAV_ROUTE_PLACES_SCREEN = "PlacesScreen"
private const val NAV_ROUTE_ERROR_SCREEN = "ErrorScreen"

const val KEY_ERROR_TEXT = "errorText"
const val KEY_RETRY_ACTION = "retryAction"

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    navController.navigate(NAV_ROUTE_PLACES_SCREEN)
                } else {
                    navController.navigate(NAV_ROUTE_GPS_DISABLED_SCREEN)
                }
            }

        val locationPermissionState = LocationPermissionState(this) {
            if (it.accessFineLocationGranted) {
                if (this.isGPSEnabled()) {
                    navController.navigate(NAV_ROUTE_PLACES_SCREEN)
                } else {
                    this.turnOnGPS(resultLauncher)
                }
            } else {
                navController.navigate(NAV_ROUTE_NO_PERMISSIONS_SCREEN)
            }
        }

        setContent {
            GeoapifyTestAppTheme {
                navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val topBarTitle =
                    rememberSaveable { mutableStateOf(resources.getString(R.string.app_name)) }
                val showBackButton = rememberSaveable { mutableStateOf(false) }

                val viewModel = viewModel<MainViewModel>(
                    factory = MainViewModelFactory(
                        PlayServicesAvailabilityChecker(
                            this, GoogleApiAvailability()
                        )
                    )
                )
                val uiState by viewModel.uiState.collectAsState()

                when (uiState) {
                    UiState.PlayServicesUnavailable -> ServiceUnavailableScreen()
                    UiState.PlayServicesAvailable -> locationPermissionState.requestPermissions()
                    else -> {}
                }

                val actionExecutor: ActionExecutor = { action ->
                    when (action) {
                        is OpenErrorScreenAction -> {
                            val bundle = Bundle()
                            bundle.putSerializable("data", action.data)
                            val resId = navController.findDestination(NAV_ROUTE_ERROR_SCREEN)?.id
                            resId?.let { navController.navigate(it, bundle) }
                        }
                    }
                }

                Scaffold(
                    scaffoldState = scaffoldState,
                    backgroundColor = Blue700,
                    topBar = {
                        TopBar(title = topBarTitle.value)
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(
                            bottom = innerPadding.calculateBottomPadding(),
                            top = innerPadding.calculateTopPadding()
                        )
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = NAV_ROUTE_INITIALIZING_SCREEN
                        ) {
                            composable(NAV_ROUTE_INITIALIZING_SCREEN) {
                                InitializingScreen()
                            }
                            composable(NAV_ROUTE_SERVICE_UNAVAILABLE_SCREEN) {
                                ServiceUnavailableScreen()
                            }
                            composable(NAV_ROUTE_GPS_DISABLED_SCREEN) {
                                GpsDisabledScreen(this@MainActivity, resultLauncher)
                            }
                            composable(NAV_ROUTE_NO_PERMISSIONS_SCREEN) {
                                NoPermissionsScreen(locationPermissionState)
                            }
                            composable(NAV_ROUTE_PLACES_SCREEN) {
                                PlacesScreen(this@MainActivity, actionExecutor)
                            }
                            composable(NAV_ROUTE_ERROR_SCREEN) { backStackEntry ->
                                val data =
                                    backStackEntry.arguments?.getSerializable("data")
                                OpenErrorScreen(data)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun OpenErrorScreen(data: Serializable?) {
        val retryAction = (data as HashMap<*, *>)[KEY_RETRY_ACTION] as RetryAction
        val errorText = data[KEY_ERROR_TEXT] as String
        ErrorScreen(errorText) {
            navController.popBackStack()
            retryAction.invoke()
        }
    }
}