package com.example.disastermanagement.navigation

import android.net.Uri

sealed class NavigationItem(open val route: String) {
    object Dashboard : NavigationItem("dashboard")
    object TrainingHub : NavigationItem("training_hub")
    object DrillSimulator : NavigationItem("drill_simulator")
    object Profile : NavigationItem("profile")
    object SafetyMap : NavigationItem("safety_map")
    object Badges : NavigationItem("badges")

    companion object {
        const val TRAINING_FILTER_ARG = "filter"
        const val DRILL_MODULE_ARG = "module"
        const val DRILL_REVIEW_ARG = "review"

        val bottomTabs = listOf(Dashboard, TrainingHub, DrillSimulator, Profile)

        fun trainingRoute(filter: String? = null): String {
            return if (filter.isNullOrBlank()) TrainingHub.route else {
                "${TrainingHub.route}?$TRAINING_FILTER_ARG=${Uri.encode(filter)}"
            }
        }

        fun drillRoute(module: String? = null, reviewMode: Boolean = false): String {
            val encodedModule = if (module.isNullOrBlank()) "" else Uri.encode(module)
            return "${DrillSimulator.route}?$DRILL_MODULE_ARG=$encodedModule&$DRILL_REVIEW_ARG=$reviewMode"
        }
    }
}
