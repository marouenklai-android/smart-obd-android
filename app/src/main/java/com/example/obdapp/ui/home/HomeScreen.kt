package com.example.obdapp.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.obdapp.bluetoothmanager.BluetoothViewModel
import com.example.obdapp.geminianalysis.viewmodel.LLMViewModel
import com.example.obdapp.ui.dtc.DtcScreenPro
import com.example.obdapp.ui.engine.CarDashboardScreen
import com.example.obdapp.ui.engine.EngineDiagnosticsScreenLight

import com.example.obdapp.ui.engine.SmartDiagnosisOverlay
import com.example.obdapp.ui.language.LanguageScreen
import com.example.obdapp.ui.navigation.Route
import com.example.obdapp.ui.settings.SettingsScreen
import com.example.obdapp.ui.settings.UnitsScreen
import com.example.obdapp.ui.theme.AccentGreen
import com.example.obdapp.ui.theme.AppBgDark
import com.example.obdapp.ui.theme.CardBackground
import kotlin.String

@Composable
fun HomeScreen(navControllerParent: NavController,viewModel: BluetoothViewModel = viewModel(),
        viewModelChat: LLMViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    val showDiagnosis by viewModelChat.showDiagnosis.collectAsState()
    println("hello showDiagnosis: $showDiagnosis")


    var showOverlay by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModelChat.diagnosisEvent.collect { event ->
            when(event) {
                LLMViewModel.DiagnosisEvent.Show -> showOverlay = true
                LLMViewModel.DiagnosisEvent.Dismiss -> showOverlay = false
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.background(AppBgDark),
            //bottomBar = { VibrantBottomBar(navController) }
            containerColor = CardBackground,
            //AppBgDark,
            bottomBar = {
                FloatingBottomBar(
                    currentRoute = currentRoute,
                    onItemSelected = { item ->
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        AppBgDark
                    )
            ) {

                NavHost(
                    navController = navController,
                    startDestination = Route.Engine.name,
                    modifier = Modifier.padding(padding)
                ) {

                    composable(Route.Engine.name) {
                        CarDashboardScreen(navControllerParent, viewModel, onUnitClick = {
                            navControllerParent.navigate(Route.OnboardingObdScreen.name)

                        },viewModelChat, onDiagnosticClick = {
                        },
                        )
                    }

                    composable(Route.Sensors.name) {

                        EngineDiagnosticsScreenLight(viewModel)

                    }
                    composable(Route.News.name) {
                        SettingsScreen(
                            onUnitClick = {
                                navController.navigate(Route.Units.name) {
                                    launchSingleTop = true
                                }
                            },
                            onLanguageClick = {
                                navController.navigate(Route.Language.name) {
                                    launchSingleTop = true
                                }
                            },
                            onThemeClick = {}
                        )
                    }
                    composable(Route.Dtc.name) {
                        DtcScreenPro( onReadClick = {}, onClearClick = {}, viewModel = viewModel)
                    }
                    composable(Route.Units.name) {
                        UnitsScreen(navController)
                    }
                    composable(Route.Language.name) {
                        LanguageScreen {
                            navController.navigate(Route.Home.name) {
                                popUpTo(Route.Language.name) { inclusive = true }
                            }
                        }

                    }
                }
            }


        }
        if (showOverlay) {
            SmartDiagnosisOverlay(
                onDismiss = { showOverlay = false }
            )
        }
//        if (showDiagnosis) {
//            SmartDiagnosisOverlay(
//                onDismiss = {
//
//                    viewModelChat.setShowDiagnosis(false)
//                }
//            )
//        }
    }
}


sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Dashboard : BottomNavItem(
        "dashboard",
        Icons.Outlined.DirectionsCar,
        Icons.Filled.DirectionsCar
    )

    object LiveData : BottomNavItem(
        "live",
        Icons.Outlined.Speed,
        Icons.Filled.Speed
    )

    object Alerts : BottomNavItem(
        "alerts",
        Icons.Outlined.Warning,
        Icons.Filled.Warning
    )

    object Logs : BottomNavItem(
        "logs",
        Icons.Outlined.List,
        Icons.Filled.Article
    )
}

@Composable
fun FloatingBottomBar(
    currentRoute: String?,
    onItemSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.LiveData,
        BottomNavItem.Alerts,
        BottomNavItem.Logs
    )

    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 24.dp),
        color = CardBackground,
            //AppBgDark,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomBarItem(
                    item = item,
                    selected = currentRoute == item.route,
                    onClick = { onItemSelected(item) }
                )
            }
        }
    }
    Spacer(Modifier.height(24.dp))

}

@Composable
private fun BottomBarItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = tween(180),
        label = "iconScale"
    )

    val color by animateColorAsState(
        targetValue = if (selected) AccentGreen else Color(0xFF007AFF).copy(alpha = 0.5f),
            ///Color(0xFF4A6FA5).copy(alpha = 0.7f),
            //AccentGreen.copy(alpha = 0.1f),
        animationSpec = tween(180),
        label = "iconColor"
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.icon,
            contentDescription = item.route,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}




