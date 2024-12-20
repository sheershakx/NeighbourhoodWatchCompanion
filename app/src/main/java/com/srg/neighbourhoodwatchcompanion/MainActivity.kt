package com.srg.neighbourhoodwatchcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import com.srg.neighbourhoodwatchcompanion.presenter.theme.NeighbourhoodWatchCompanionTheme
import com.srg.neighbourhoodwatchcompanion.presenter.ui.auth.LoginScreen
import com.srg.neighbourhoodwatchcompanion.presenter.ui.auth.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeighbourhoodWatchCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  RootView(innerPadding)
                }
            }
        }
    }
}

@Composable
fun RootView(innerPadding: PaddingValues) {
    Column(modifier = Modifier.padding(innerPadding)) {
        //attach any view as per app state and requirement
        LoginScreen()
//        RegisterScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NeighbourhoodWatchCompanionTheme {
//        Greeting("hello Sheershak")
    }
}