package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.srg.neighbourhoodwatchcompanion.AppNavigatorImpl
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.widgets.CustomBottomAndTopBars
import com.srg.neighbourhoodwatchcompanion.presenter.theme.YellowCard
import com.srg.neighbourhoodwatchcompanion.presenter.theme.colors
import com.srg.neighbourhoodwatchcompanion.presenter.ui.NavGraphs
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.HomeViewScreenDestination
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@BottomNavGraph(start = true)
@Composable
fun DashboardScreen() {
    val navHostController = rememberNavController()
    val customBottomTopBarObject = CustomBottomAndTopBars

    navHostController.addOnDestinationChangedListener { controller, _, _ ->
        val routes = controller
            .currentBackStack.value.joinToString(", ") { it.destination.route.toString() }

        Timber.d("BackStackLog BackStack: $routes")
    }
    BackHandler {
        Timber.d("Back Pressed :Dashboard= ${navHostController.currentBackStackEntry?.destination?.displayName}")
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            customBottomTopBarObject.CustomTopAppBar(
                navController = navHostController
            )
        },
        bottomBar = {
            customBottomTopBarObject.BottomNavigationBar(navHostController)
        },
    ) { innerPadding ->
        DestinationsNavHost(
            navController = navHostController,
            navGraph = NavGraphs.bottom.copy(startRoute = HomeViewScreenDestination),
            modifier = Modifier
                .fillMaxSize()
                .background(color = colors.surface)
                .padding(innerPadding),
            engine = rememberNavHostEngine(
                rootDefaultAnimations = RootNavGraphDefaultAnimations(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start, tween(
                                300
                            )
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start, tween(
                                300
                            )
                        )
                    },
                )
            ),

            dependenciesContainerBuilder = {
                //todo:: vvi for error related to DI with APp Navigator
                dependency(AppNavigatorImpl(destinationsNavigator, navHostController))
            }
        )
    }
}





