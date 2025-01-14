package com.srg.neighbourhoodwatchcompanion

import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import com.srg.neighbourhoodwatchcompanion.presenter.ui.NavGraphs
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.DashboardScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.HomeViewScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.LoginScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.MapViewScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.RegisterScreenDestination


interface AppNavigator {
    fun openRegisterScreen()
    fun openLoginScreen()
    fun openDashboardScreen()
    fun showHomeScreen()
    fun showMapScreen()
    fun bottomNavNavigation(route: Direction)
}

class AppNavigatorImpl constructor(
    val destinationNavigator: DestinationsNavigator,
    val navHostController: NavHostController
) :
    AppNavigator {

    override fun openRegisterScreen() {
        destinationNavigator.navigate(RegisterScreenDestination) {
            popUpTo(LoginScreenDestination) { inclusive = false }
        }

    }

    override fun openLoginScreen() {
        TODO("Not yet implemented")
    }

    override fun openDashboardScreen() {
        destinationNavigator.navigate(DashboardScreenDestination) {
            popUpTo(LoginScreenDestination) {
                inclusive = true
            }
        }
    }

    override fun showHomeScreen() {
        destinationNavigator.navigate(HomeViewScreenDestination) {
            popUpTo(DashboardScreenDestination) {
                saveState = true
//                        }
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    }

    override fun showMapScreen() {
        destinationNavigator.navigate(MapViewScreenDestination) {
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    override fun bottomNavNavigation(route: Direction) {
        destinationNavigator.navigate(route) {
            popUpTo(NavGraphs.bottom.startRoute) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}


@NavGraph
@Destination
annotation class BottomNavGraph(
    val start: Boolean=false
)
