package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.srg.neighbourhoodwatchcompanion.AppNavigatorImpl
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.widgets.BottomNavigationBar
import com.srg.neighbourhoodwatchcompanion.presenter.ui.NavGraphs
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.HomeViewScreenDestination
import timber.log.Timber

@SuppressLint("RestrictedApi")
@BottomNavGraph(start = true)
@Composable
fun DashboardScreen(
) {
    val navHostController = rememberNavController()

    navHostController.addOnDestinationChangedListener { controller, _, _ ->
        val routes = controller
            .currentBackStack.value
            .map { it.destination.route }
            .joinToString(", ")

        Timber.d("BackStackLog BackStack: $routes")
    }
    BackHandler {
        Timber.d("Back Pressed :Dashboard= ${navHostController.currentBackStackEntry?.destination?.route}")
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navHostController)
        },
    ) { innerPadding ->

        DestinationsNavHost(
            navController = navHostController,
            navGraph = NavGraphs.bottom.copy(startRoute = HomeViewScreenDestination),
            modifier = Modifier.padding(innerPadding),
            engine = rememberNavHostEngine(
                rootDefaultAnimations = RootNavGraphDefaultAnimations(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start, tween(
                                700
                            )
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start, tween(
                                700
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




