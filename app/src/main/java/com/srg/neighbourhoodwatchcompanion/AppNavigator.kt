package com.srg.neighbourhoodwatchcompanion

import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.AccountEditScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.ContactUsScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.DashboardScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.HomeViewScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.IncidentFormScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.LocationInputScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.LoginScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.MapViewScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.PrivacyPolicyScreenDestination
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.RegisterScreenDestination


interface AppNavigator {
    fun openRegisterScreen()
    fun openLoginScreen()
    fun openDashboardScreen()
    fun showHomeScreen()
    fun showMapScreen()
    fun openIncidentFormScreen()
    fun navigateBack()
    fun openLocationInputScreen()
    fun openAccountEditScreen()
    fun openContactUsScreen()
    fun openPrivacyPolicyScreen()
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
        destinationNavigator.navigate(RegisterScreenDestination) {
            popUpTo(DashboardScreenDestination) { inclusive = true }
        }
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
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    override fun showMapScreen() {
        destinationNavigator.navigate(MapViewScreenDestination) {
            launchSingleTop = true
            restoreState = true
        }
    }

    override fun openIncidentFormScreen() {
        destinationNavigator.navigate(IncidentFormScreenDestination) {
            popUpTo(HomeViewScreenDestination) {
                launchSingleTop = true
            }
        }
    }

    override fun navigateBack() {
        destinationNavigator.popBackStack()
    }

    override fun openLocationInputScreen() {
        destinationNavigator.navigate(LocationInputScreenDestination)
    }

    override fun openAccountEditScreen() {
        destinationNavigator.navigate(AccountEditScreenDestination)
    }

    override fun openContactUsScreen() {
        destinationNavigator.navigate(ContactUsScreenDestination)
    }

    override fun openPrivacyPolicyScreen() {
        destinationNavigator.navigate(PrivacyPolicyScreenDestination)
    }

}

@RootNavGraph
@NavGraph
@Destination
annotation class BottomNavGraph(
    val start: Boolean = false
)

@BottomNavGraph
@NavGraph
annotation class ActionNavGraph(
    val start: Boolean = false
)

