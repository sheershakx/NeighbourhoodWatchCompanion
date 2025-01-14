package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.widgets.BottomNavigationBar
import com.srg.neighbourhoodwatchcompanion.presenter.ui.NavGraphs
import com.srg.neighbourhoodwatchcompanion.presenter.ui.destinations.HomeViewScreenDestination

@BottomNavGraph(start = true)
@Composable
fun DashboardScreen(
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.bottom.copy(startRoute = HomeViewScreenDestination),
            modifier = Modifier.padding(innerPadding)
        )
    }
}



