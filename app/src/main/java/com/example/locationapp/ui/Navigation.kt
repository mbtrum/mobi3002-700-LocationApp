package com.example.locationapp.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.locationapp.MainViewModel
import com.example.locationapp.ui.screens.LocationScreen
import com.example.locationapp.ui.screens.MapScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    // Navigation
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) } // default set to 0

    // Initialize view model here and pass into screens
    val viewModel: MainViewModel = viewModel()

    // Begin: Ask permission to get device location.
    // Note: If permission is previously granted, the dialog won't appear and refreshLocation() will get called.
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.values.any { it }) {
            viewModel.refreshLocation() // call a function to get the current location for device
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }
    // End: Ask permission to get device location.

    // Main display (top bar, screens, bottom nav bar)
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Location App")
                }
            )
        },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets)
            {
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.Map, contentDescription = "Map") },
                    label = { Text("Map") },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        navController.navigate(route = "map")
                    })

                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Locations") },
                    label = { Text("Locations") },
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate(route = "locations")
                    })
            }
        }
    )
    { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "map",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "map") {
                MapScreen(viewModel)
            }
            composable(route = "locations") {
                LocationScreen(viewModel)
            }
        }
    }
}