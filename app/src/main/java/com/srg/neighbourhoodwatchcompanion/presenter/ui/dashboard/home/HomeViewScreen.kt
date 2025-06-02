package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.R
import com.srg.neighbourhoodwatchcompanion.common.LargeSpacer
import com.srg.neighbourhoodwatchcompanion.common.MediumSpacer

@SuppressLint("RestrictedApi")
@BottomNavGraph
@Composable
fun HomeViewScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    appNavigator: AppNavigator,
) {
    val uiState by viewModel.uiState.collectAsState()
    val firstName by viewModel.userName.collectAsState()


    LaunchedEffect(uiState) {
        when (uiState) {
            is BaseViewState.Data -> {
                val data = uiState.cast<BaseViewState.Data<HomeState>>().value
                if (data.openIncidentForm == true) {
                    appNavigator.openIncidentFormScreen()
                    viewModel.clearState()
                }
            }

            else -> {}
        }
    }

    //todo (each time home is clicked (not the case of pressing back from mapview tab), the app is calling api for user_name
    // reduce the time, either by remembering or saving to the savedState)
    Column(modifier = Modifier.fillMaxSize()) {
        //Greetings View
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Hi, $firstName",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp)
                    .align(Alignment.CenterVertically),
                style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold)
            )

            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                null,
                modifier = Modifier
                    .padding(10.dp)
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
            )

        }
        MediumSpacer()
        Text(
            "Keeping community safe is our priority. Report any incidents you encounter.",
            modifier = Modifier.padding(horizontal = 8.dp),
            style = TextStyle(fontSize = 17.sp, fontStyle = FontStyle.Italic),
            color = Color.DarkGray
        )
        LargeSpacer()


        //Data dashboard

        Row(modifier = Modifier.fillMaxWidth()) {
            CardView(
                Modifier
                    .weight(1f)
                    .padding(10.dp)
            )
            CardView(
                Modifier
                    .weight(1f)
                    .padding(10.dp)
            )
        }
        MediumSpacer()

        //Incident reports
        CardView(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        LargeSpacer()

        //Incident report action button

        Button(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp),
            colors = ButtonDefaults.buttonColors(),
            shape = RoundedCornerShape(30),
            onClick = {
                viewModel.onTriggerEvent(HomeEvent.IncidentFormButtonClicked)
            }) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.AddCircle, null, modifier = Modifier.size(30.dp))
                Text(
                    "Report Incident",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                )


            }
        }


    }
}

@Composable
fun CardView(modifier: Modifier) {
    Card(
        modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                text = "Incidents This week"
            )
            MediumSpacer()
            Text(

                text = "23"
            )
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeViewScreen()
//}