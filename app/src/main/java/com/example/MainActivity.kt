package com.example

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = viewModel()
            val userProfile by viewModel.userProfile.collectAsState()

            MyApplicationTheme(darkTheme = userProfile.isDarkTheme) {
                MainAppLayout(viewModel = viewModel)
            }
        }
    }
}

enum class Screen(val title: String, val icon: ImageVector, val tag: String) {
    Home("Home", Icons.Default.Home, "nav_home"),
    JadwalSalat("Jadwal", Icons.Default.AccessTime, "nav_jadwal"),
    Artikel("Artikel", Icons.Default.Article, "nav_artikel"),
    Hadis("Hadis", Icons.Default.Book, "nav_hadis"),
    Profil("Profil", Icons.Default.Person, "nav_profil")
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainAppLayout(viewModel: AppViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    // Request Location Permissions Automatically using Accompanist
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        if (locationPermissionState.allPermissionsGranted && userProfile.isGpsEnabled) {
            viewModel.requestGpsLocation()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                Screen.values().forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        modifier = Modifier.testTag(screen.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Home -> HomeScreen(
                    viewModel = viewModel,
                    userProfile = userProfile,
                    onNavigateToPrayerTimes = { currentScreen = Screen.JadwalSalat }
                )
                Screen.JadwalSalat -> PrayerScheduleScreen(
                    viewModel = viewModel,
                    userProfile = userProfile
                )
                Screen.Artikel -> DakwahArticlesScreen(
                    viewModel = viewModel
                )
                Screen.Hadis -> HadithSearchScreen(
                    viewModel = viewModel
                )
                Screen.Profil -> UserProfileScreen(
                    viewModel = viewModel,
                    userProfile = userProfile
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
