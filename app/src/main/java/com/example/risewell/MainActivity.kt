package com.example.risewell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.risewell.data.model.Persona
import com.example.risewell.ui.screens.chat.ChatScreen
import com.example.risewell.ui.screens.home.HomeScreen
import com.example.risewell.ui.screens.profile.ProfileScreen
import com.example.risewell.ui.screens.profile.ProfileViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.example.risewell.ui.theme.RiseWellTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RiseWellTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") {
                                HomeScreen(
                                    onPersonaSelected = { persona ->
                                        navController.navigate("chat/${persona.name}")
                                    },
                                    onProfileClick = {
                                        navController.navigate("profile")
                                    }
                                )
                            }
                            composable("profile") {
                                val profileViewModel: ProfileViewModel = hiltViewModel()
                                val userProfile by profileViewModel.userProfile.collectAsState(initial = null)

                                ProfileScreen(
                                    userProfile = userProfile,
                                    onSaveProfile = { profile -> profileViewModel.saveProfile(profile) },
                                    onNavigateBack = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                            composable("chat/{persona}") { backStackEntry ->
                                val personaName = backStackEntry.arguments?.getString("persona")
                                val persona = personaName?.let {
                                    try {
                                        Persona.valueOf(it)
                                    } catch (_: IllegalArgumentException) {
                                        Persona.COACH
                                    }
                                } ?: Persona.COACH

                                ChatScreen(
                                    persona = persona,
                                    onNavigateBack = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}