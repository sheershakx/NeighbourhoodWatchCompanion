package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph

@BottomNavGraph
@Composable
fun HomeViewScreen(){
    Scaffold {
        innerPadding->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text("This is a home screen")
        }

    }
}