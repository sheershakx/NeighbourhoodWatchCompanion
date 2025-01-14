package com.srg.neighbourhoodwatchcompanion.common.widgets


import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.srg.neighbourhoodwatchcompanion.presenter.ui.NavGraphs
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.HomeViewScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.MapViewScreenDestination

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Home", HomeViewScreenDestination, Icons.Filled.Home),
    TopLevelRoute("Map", MapViewScreenDestination, Icons.Filled.LocationOn),
    TopLevelRoute("Test", MapViewScreenDestination, Icons.Filled.LocationOn)
)

@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val destinationsNavigator=navController.toDestinationsNavigator()
    NavigationBar {
        val currentBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry.value?.destination
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(icon = {
                Icon(
                    topLevelRoute.icon, contentDescription = topLevelRoute.name
                )
            },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination==topLevelRoute.route,
                onClick = {
                    destinationsNavigator.navigate(topLevelRoute.route) {
                        popUpTo(NavGraphs.bottom.startRoute) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}

