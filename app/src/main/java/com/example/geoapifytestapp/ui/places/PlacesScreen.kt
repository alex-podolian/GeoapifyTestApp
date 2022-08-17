package com.example.geoapifytestapp.ui.places

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoapifytestapp.KEY_RETRY_ACTION
import com.example.geoapifytestapp.action.OpenErrorScreenAction
import com.example.geoapifytestapp.action.RetryAction
import com.example.geoapifytestapp.action.contract.ActionExecutor
import com.example.geoapifytestapp.core.data.api.LocationApi
import com.example.geoapifytestapp.core.data.model.Place
import com.example.geoapifytestapp.core.presentation.PlacesEffect
import com.example.geoapifytestapp.core.presentation.PlacesIntent
import com.example.geoapifytestapp.core.presentation.PlacesState
import com.example.geoapifytestapp.ui.LoadingIndicator
import com.example.geoapifytestapp.ui.theme.Blue370
import com.example.geoapifytestapp.utils.getBounds
import com.google.android.gms.location.LocationServices

@Composable
fun PlacesScreen(
    context: Context,
    executor: ActionExecutor? = null
) {
    val locationApi = LocationApi(LocationServices.getFusedLocationProviderClient(context))
    val viewModel = viewModel<PlacesViewModel>(factory = PlacesViewModelFactory(locationApi))
    val state by viewModel.state.collectAsState(initial = PlacesState())

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PlacesEffect.NavigateToErrorScreen -> executor?.let {
                    val retryAction = object : RetryAction() {
                        override fun invoke() {
                            viewModel.dispatch(PlacesIntent.GetCurrentLocation(locationApi))
                        }
                    }
                    effect.data[KEY_RETRY_ACTION] = retryAction
                    it(OpenErrorScreenAction(effect.data))
                }
            }
        }
    }

    if (state.isLoading) {
        LoadingIndicator(Modifier.fillMaxSize())
        return
    }

    val lazyListState = rememberLazyListState()
    state.places?.let { places ->
        if (places.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 4.dp),
                state = lazyListState
            ) {
                itemsIndexed(places) { _, item ->
                    PlaceListItem(item)
                }
            }
        }
        return
    }

    state.location?.let {
        val bounds = it.getBounds()
        viewModel.dispatch(PlacesIntent.LoadPlaces(bounds))
    }
}

@Composable
fun PlaceListItem(
    place: Place
) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 6.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(color = Blue370, shape = RoundedCornerShape(2.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                place.details?.name?.let {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = it,
                        style = TextStyle(
                            color = White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                place.details?.street?.let {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = it,
                        style = TextStyle(
                            color = White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(color = Blue370, shape = RoundedCornerShape(2.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                place.details?.data?.description?.opening_hours?.let {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = it,
                        style = TextStyle(
                            color = White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                place.details?.data?.description?.phone?.let {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = it,
                        style = TextStyle(
                            color = White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(color = Blue370, shape = RoundedCornerShape(2.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                place.details?.data?.description?.website?.let {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = it,
                        style = TextStyle(
                            color = White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}