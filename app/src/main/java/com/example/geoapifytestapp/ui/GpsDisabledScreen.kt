package com.example.geoapifytestapp.ui

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoapifytestapp.R
import com.example.geoapifytestapp.ui.theme.Blue800
import com.example.geoapifytestapp.ui.theme.Teal100
import com.example.geoapifytestapp.utils.turnOnGPS

@Composable
fun GpsDisabledScreen(
    context: Context,
    resultLauncher: ActivityResultLauncher<IntentSenderRequest>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            Icons.Filled.Warning,
            tint = Teal100,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = stringResource(id = R.string.gps_disabled),
            style = MaterialTheme.typography.h6,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 36.dp, end = 36.dp)
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Teal100),
            shape = RoundedCornerShape(7F, 7F, 7F, 7F),
            contentPadding = PaddingValues(bottom = 0.dp),
            onClick = { context.turnOnGPS(resultLauncher) }
        ) {
            Text(
                text = "TURN ON",
                style = TextStyle(
                    color = Blue800,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}