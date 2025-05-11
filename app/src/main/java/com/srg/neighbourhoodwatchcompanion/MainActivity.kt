package com.srg.neighbourhoodwatchcompanion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.srg.neighbourhoodwatchcompanion.common.showToast
import com.srg.neighbourhoodwatchcompanion.presenter.theme.NeighbourhoodWatchCompanionTheme
import com.srg.neighbourhoodwatchcompanion.presenter.ui.NavGraphs
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.DashboardScreenDestination
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var supabaseAuth: Auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var showSplashScreen: Boolean = true
        var isLoggedIn: Boolean = false
        installSplashScreen().setKeepOnScreenCondition {
            showSplashScreen
        }
        val finish: () -> Unit = backPressHandler()

        lifecycleScope.launch {
            isLoggedIn = supabaseAuth.loadFromStorage()
        }.invokeOnCompletion {
            showSplashScreen = false
            setContent {
                NeighbourhoodWatchCompanionTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
                            NavigationBarDefaults.windowInsets
                        )
                    ) { innerPadding ->
                        RootView(innerPadding, finish, isLoggedIn)

                    }
                }
            }
        }
        enableEdgeToEdge()

    }

    private fun backPressHandler(): () -> Unit {
        var backPressed = 0L
        val finish: () -> Unit = {
            if (backPressed + 3000 > System.currentTimeMillis()) {
                finishAndRemoveTask()
            } else {
                showToast("Press back again to exit")
            }
            backPressed = System.currentTimeMillis()
        }
        return finish
    }


}

@SuppressLint("RestrictedApi")
@Composable
fun RootView(
    innerPadding: PaddingValues, finish: () -> Unit, isLoggedIn: Boolean,
) {

    //TODO(
    // Fix the navigation backHandler issue..
    // is not navigating back in bottom nav screens and
    // not asking for app exit confirmation and exitting app
    // and is acting different when splash-> dashboard vs
    // from login->dashboard)
    var navHostController = rememberNavController()
    val currentBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentDestination =
        currentBackStackEntry.value?.destination?.route ?: NavGraphs.root.startRoute
    val startRoute = if (isLoggedIn) DashboardScreenDestination else NavGraphs.root.startRoute
    val startGraph = if (isLoggedIn) NavGraphs.bottom else NavGraphs.root



    BackHandler {
        Timber.d("Back pressed main: ${navHostController.currentBackStackEntry?.destination?.route}")

        Timber.wtf("NAVGRAPH BOTTOM ROUTE=${NavGraphs.bottom.startRoute.route}")
        // Check if the current screen is the root screen, and if so, handle the back press
        if (currentBackStackEntry.value?.destination?.route == startGraph.startRoute.route) {
            finish()
        } else {
            // Allow normal back navigation
            navHostController.popBackStack()
        }
    }

    Column(modifier = Modifier.padding(innerPadding)) {
        //attach any view as per app state and requirement
        DestinationsNavHost(
            navController = navHostController,
            navGraph = startGraph, // Auto-generated navigation graph
            dependenciesContainerBuilder = {
                dependency(
                    AppNavigatorImpl(
                        destinationsNavigator,
                        navHostController
                    )
                )
            }
        ) {

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NeighbourhoodWatchCompanionTheme {
//        Greeting("hello Sheershak")
    }
}