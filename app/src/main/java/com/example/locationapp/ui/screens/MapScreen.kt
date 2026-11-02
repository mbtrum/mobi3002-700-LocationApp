package com.example.locationapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.locationapp.MainViewModel

@Composable
fun MapScreen(viewModel: MainViewModel) {

    val location = viewModel.location

    Column() {
        Text("Map Screen")

        location?.let {
            Text("Longitude: ${location.longitude}")
            Text("Latitude: ${location.latitude}")
        }
    }

}