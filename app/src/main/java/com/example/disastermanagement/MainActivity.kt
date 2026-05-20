package com.example.disastermanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.disastermanagement.navigation.NavigationItem
import com.example.disastermanagement.ui.components.BottomNavBar
import com.example.disastermanagement.ui.components.DisasterTrainingTopAppBar
import com.example.disastermanagement.ui.screens.DashboardScreen
import com.example.disastermanagement.ui.screens.FullBadgesScreen
import com.example.disastermanagement.ui.screens.DrillSimulatorScreen
import com.example.disastermanagement.ui.screens.ProfileScreen
import com.example.disastermanagement.ui.screens.SafetyMapScreen
import com.example.disastermanagement.ui.screens.TrainingHubScreen
import com.example.disastermanagement.ui.state.AppViewModel
import com.example.disastermanagement.ui.theme.DisasterManagementTheme
import androidx.navigation.NavGraph.Companion.findStartDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DisasterManagementTheme {
                DisasterManagementApp()
            }
        }
    }
}

@Composable
fun DisasterManagementApp() {
    val navController = rememberNavController()
    val appViewModel: AppViewModel = viewModel()
    val uiState by appViewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route?.substringBefore("?") ?: NavigationItem.Dashboard.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DisasterTrainingTopAppBar()
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { item ->
                    val targetRoute = when (item) {
                        NavigationItem.DrillSimulator -> NavigationItem.drillRoute(module = appViewModel.defaultDrillModule())
                        else -> item.route
                    }
                    navController.navigate(targetRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavigationItem.Dashboard.route
            ) {
                composable(NavigationItem.Dashboard.route) {
                    DashboardScreen(
                        readinessScore = uiState.readinessScore,
                        drillsCompleted = uiState.drillsCompleted,
                        tipsRead = uiState.tipsRead,
                        onOpenSafetyMap = { navController.navigate(NavigationItem.SafetyMap.route) },
                        onOpenTrainingHub = { navController.navigate(NavigationItem.TrainingHub.route) },
                        onReviewEarthquake = { navController.navigate(NavigationItem.trainingRoute("Earthquake")) }
                    )
                }
                composable(
                    route = "${NavigationItem.TrainingHub.route}?${NavigationItem.TRAINING_FILTER_ARG}={${NavigationItem.TRAINING_FILTER_ARG}}",
                    arguments = listOf(
                        navArgument(NavigationItem.TRAINING_FILTER_ARG) {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { entry ->
                    TrainingHubScreen(
                        modules = uiState.modules,
                        filters = appViewModel.trainingFilters(),
                        initialFilter = entry.arguments?.getString(NavigationItem.TRAINING_FILTER_ARG),
                        onStartModule = { moduleTitle ->
                            navController.navigate(NavigationItem.drillRoute(module = moduleTitle))
                        },
                        onReviewModule = { moduleTitle ->
                            navController.navigate(NavigationItem.drillRoute(module = moduleTitle, reviewMode = true))
                        }
                    )
                }
                composable(
                    route = "${NavigationItem.DrillSimulator.route}?${NavigationItem.DRILL_MODULE_ARG}={${NavigationItem.DRILL_MODULE_ARG}}&${NavigationItem.DRILL_REVIEW_ARG}={${NavigationItem.DRILL_REVIEW_ARG}}",
                    arguments = listOf(
                        navArgument(NavigationItem.DRILL_MODULE_ARG) {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                        navArgument(NavigationItem.DRILL_REVIEW_ARG) {
                            type = NavType.BoolType
                            defaultValue = false
                        }
                    )
                ) { entry ->
                    val moduleName = entry.arguments?.getString(NavigationItem.DRILL_MODULE_ARG)
                        ?.takeIf { it.isNotBlank() }
                        ?: appViewModel.defaultDrillModule()
                    val reviewMode = entry.arguments?.getBoolean(NavigationItem.DRILL_REVIEW_ARG) ?: false
                    val drillScenario = appViewModel.drillScenarioFor(moduleName)
                    DrillSimulatorScreen(
                        moduleName = moduleName,
                        reviewMode = reviewMode,
                        questions = drillScenario.questions,
                        imageUrl = drillScenario.imageUrl,
                        onDrillCompleted = { appViewModel.completeDrill(moduleName) },
                        onBackToLearn = {
                            navController.navigate(NavigationItem.TrainingHub.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(NavigationItem.Profile.route) {
                    ProfileScreen(
                        readinessScore = uiState.readinessScore,
                        modules = uiState.modules,
                        drillsCompleted = uiState.drillsCompleted,
                        badges = uiState.badges,
                        onViewAllBadges = { navController.navigate(NavigationItem.Badges.route) }
                    )
                }
                composable(NavigationItem.SafetyMap.route) {
                    SafetyMapScreen(safeZones = uiState.safeZones)
                }
                composable(NavigationItem.Badges.route) {
                    FullBadgesScreen(badges = uiState.badges)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DisasterManagementAppPreview() {
    DisasterManagementTheme {
        DisasterManagementApp()
    }
}
