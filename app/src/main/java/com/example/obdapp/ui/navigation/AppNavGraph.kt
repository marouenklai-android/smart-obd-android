package com.example.obdapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.obdapp.ui.bluetooth.BluetoothFlowScreen
import com.example.obdapp.bluetoothmanager.BluetoothViewModel
import com.example.obdapp.geminianalysis.viewmodel.LLMViewModel
import com.example.obdapp.ui.diagnosticreport.ProDiagnosticReportScreen
import com.example.obdapp.ui.engine.CarDashboardScreen
import com.example.obdapp.ui.home.HomeScreen
import com.example.obdapp.ui.language.LanguageScreen
import com.example.obdapp.ui.oboardingobd.ObdOnboardingScreen
import com.example.obdapp.ui.onboarding.OnboardingScreen
import com.example.obdapp.ui.settings.SettingsScreen
import com.example.obdapp.ui.settings.UnitsScreen
import androidx.compose.runtime.collectAsState

@Composable
fun AppNavGraph(
    languageSelected: Boolean
) {
    val navController = rememberNavController()
    val sharedViewModel = viewModel<BluetoothViewModel>()
    val sharedLLMViewModel = viewModel<LLMViewModel>()



    NavHost(
        navController = navController,
        startDestination = if (languageSelected)
            Route.Home.name
        else
            Route.OnboardingScreen.name
    ) {
        composable(Route.OnboardingScreen.name) {
            OnboardingScreen {
                navController.navigate(Route.Language.name) {
                    popUpTo(Route.OnboardingScreen.name) { inclusive = true }
                }
            }

        }
        composable(Route.OnboardingObdScreen.name) {
            ObdOnboardingScreen {
                navController.navigate(Route.Bluetooth.name) {
                    //popUpTo(Route.OnboardingScreen.name) { inclusive = true }
                }
            }

        }

        composable(Route.Language.name) {
                LanguageScreen {
                    navController.navigate(Route.OnboardingObdScreen.name) {
                        popUpTo(Route.Language.name) { inclusive = true }
                    }
                }

        }
        composable(Route.Engine.name) {
            CarDashboardScreen(navController,viewModel = sharedViewModel, onUnitClick = {
                navController.navigate(Route.OnboardingObdScreen.name)
            },
                onDiagnosticClick = {})
        }
        composable(Route.Bluetooth.name) {
            BluetoothFlowScreen(navController,sharedViewModel)
        }
        composable(Route.Home.name) {
            HomeScreen(navController,viewModel = sharedViewModel, viewModelChat =  sharedLLMViewModel)
        }
        composable(Route.Report.name) {
            val result=sharedLLMViewModel.getLatestDiagnostic.collectAsState().value
            if(result != null) {
                ProDiagnosticReportScreen(result)
            }
        }

        composable(Route.News.name) {
            SettingsScreen (onUnitClick = {
                navController.navigate(Route.Units.name)
            }
            ,
                onLanguageClick = {
                    navController.navigate("language")
                },
                onThemeClick = {                        })
        }
        composable(Route.Units.name) {
            UnitsScreen(navController)
        }
    }
}
