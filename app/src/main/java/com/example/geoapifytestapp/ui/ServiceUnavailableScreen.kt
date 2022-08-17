package com.example.geoapifytestapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.geoapifytestapp.R
import com.example.geoapifytestapp.ui.theme.Teal100

@Composable
fun ServiceUnavailableScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.play_services_unavailable),
            style = MaterialTheme.typography.h6,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Icon(
            Icons.Filled.Warning,
            tint = Teal100,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}
