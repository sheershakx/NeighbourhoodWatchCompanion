package com.srg.neighbourhoodwatchcompanion.common.widgets


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.HomeViewScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.MapViewScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.SettingsViewScreenDestination
import timber.log.Timber

data class BottomNavigationScreens<T : Any>(
    val name: String,
    val destination: T,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavigationScreens = listOf(
    BottomNavigationScreens(
        "Home",
        HomeViewScreenDestination,
        Icons.Filled.Home,
        Icons.Outlined.Home
    ),
    BottomNavigationScreens(
        "Mapview",
        MapViewScreenDestination,
        Icons.Filled.LocationOn,
        Icons.Outlined.LocationOn
    ),
    BottomNavigationScreens(
        "Settings",
        SettingsViewScreenDestination,
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    )
)

object CustomBottomAndTopBars {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    internal fun CustomTopAppBar(
        navController: NavController,
    ) {
        val currentBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry.value?.destination
        val showTopAppBar = !bottomNavigationScreens.any {
            it.destination.route == currentDestination?.route
        }
        if (showTopAppBar) {
            Column {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .consumeWindowInsets(paddingValues = PaddingValues(30.dp)),
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = Color.Black,
                    ),
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                )
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Composable
    internal fun BottomNavigationBar(navController: NavController) {
        val destinationsNavigator = navController.toDestinationsNavigator()
        val currentBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry.value?.destination
        val selectedDestination = currentDestination?.route
        val showBottomNavBar = bottomNavigationScreens.any {
            it.destination.route == currentDestination?.route
        }
        if (showBottomNavBar) {
            NavigationBar {
                bottomNavigationScreens.forEach { topLevelRoute ->
                    val isSelected =
                        selectedDestination?.equals(topLevelRoute.destination.route) == true
                    Timber.d("Navigation bar sroute : $selectedDestination")
                    Timber.d("Navigation bar : ${topLevelRoute.destination.route}")
                    NavigationBarItem(icon = {
                        Icon(
                            if (isSelected) topLevelRoute.selectedIcon else topLevelRoute.unselectedIcon,
                            contentDescription = topLevelRoute.name
                        )
                    },
                        label = { Text(topLevelRoute.name) },
                        selected = isSelected,
                        onClick = {
                            destinationsNavigator.navigate(topLevelRoute.destination) {

                                popUpTo(HomeViewScreenDestination) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                }
            }
        }
    }
}

