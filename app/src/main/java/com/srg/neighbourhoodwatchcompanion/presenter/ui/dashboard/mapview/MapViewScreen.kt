package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.mapview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph

@BottomNavGraph
@Composable
fun MapViewScreen(){
        Column(modifier = Modifier.fillMaxSize()) {
            Text("This is a map screen")
        }
}