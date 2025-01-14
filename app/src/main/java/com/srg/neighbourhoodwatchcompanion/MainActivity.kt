package com.srg.neighbourhoodwatchcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

@Composable
fun RootView(
    innerPadding: PaddingValues, finish: () -> Unit, isLoggedIn: Boolean,
) {
    var navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination =
        currentBackStackEntry.value?.destination?.route ?: NavGraphs.root.startRoute
    val startRoute = if (isLoggedIn) DashboardScreenDestination else NavGraphs.root.startRoute
    val startGraph = if (isLoggedIn) NavGraphs.bottom else NavGraphs.root


    Timber.i("NAVGRAPH ROUTE=${currentDestination}")
    Timber.i("NAVGRAPH sROUTE=${startRoute}")
    Timber.i("NAVGRAPH BOTTOM ROUTE=${startGraph}")

    BackHandler {
        Timber.wtf("NAVGRAPH BOTTOM ROUTE=${NavGraphs.bottom.startRoute.route}")
        // Check if the current screen is the root screen, and if so, handle the back press
        if (navController.currentDestination?.route == startGraph.startRoute.route) {
            finish()
        } else {
            // Allow normal back navigation
            navController.popBackStack()
        }
    }

    Column(modifier = Modifier.padding(innerPadding)) {
        //attach any view as per app state and requirement
        DestinationsNavHost(
            navController = navController,
            navGraph = startGraph, // Auto-generated navigation graph
            dependenciesContainerBuilder = {
                dependency(AppNavigatorImpl(destinationsNavigator, navController))
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NeighbourhoodWatchCompanionTheme {
//        Greeting("hello Sheershak")
    }
}