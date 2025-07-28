package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.SmallSpacer
import com.srg.neighbourhoodwatchcompanion.common.StringResources.DASHBOARD_MESSAGE
import com.srg.neighbourhoodwatchcompanion.common.formatDateTimeForDisplay
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Black
import com.srg.neighbourhoodwatchcompanion.presenter.theme.LightGray
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Pink40
import com.srg.neighbourhoodwatchcompanion.presenter.theme.PinkCard
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Purple40
import com.srg.neighbourhoodwatchcompanion.presenter.theme.RedCard
import com.srg.neighbourhoodwatchcompanion.presenter.theme.WarningColor
import com.srg.neighbourhoodwatchcompanion.presenter.theme.YellowCard

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RestrictedApi")
@BottomNavGraph
@Composable
fun HomeViewScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    appNavigator: AppNavigator,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val firstName by viewModel.userName.collectAsState()
    val profileImage by viewModel.profileImage.collectAsState()
    val detailedIncidents by viewModel.detailedIncidents.collectAsState()

    val defaultCardColor = CardColors(
        containerColor = Color.LightGray,
        contentColor = Color.Black,
        disabledContainerColor = Color.LightGray,
        disabledContentColor = Color.Black
    )


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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        //Greetings View
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    "Hi, $firstName",
                    style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = DASHBOARD_MESSAGE,
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(fontSize = 14.sp, fontStyle = FontStyle.Italic),
                    color = Color.DarkGray
                )
            }

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(profileImage)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(10.dp)
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
            )

        }
        //Data dashboard

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CardView(
                Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp),
                defaultCardColor.copy(containerColor = YellowCard),
                "Incident this week",
                "23"
            )
            CardView(
                Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp),
                defaultCardColor.copy(containerColor = RedCard),
                "Casualties this week", "23"
            )
        }

        //Incident reports
        CardView(
            Modifier
                .fillMaxWidth()

                .padding(vertical = 6.dp),
            defaultCardColor.copy(containerColor = PinkCard),
            "Total Incident reports", "234"
        )

        //Incident report action button

        Button(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .height(IntrinsicSize.Min),
            colors = ButtonDefaults.buttonColors(),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp),
            border = BorderStroke(
                width = 1.dp, brush = Brush.horizontalGradient(
                    listOf(
                        Purple40,
                        WarningColor, YellowCard, Pink40
                    )
                )
            ),
            shape = RoundedCornerShape(60),
            onClick = {
                viewModel.onTriggerEvent(HomeEvent.IncidentFormButtonClicked)
            }) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.AddCircle, null, modifier = Modifier.size(18.dp))
                Text(
                    "Report Incident",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )


            }
        }
        SmallSpacer()

        Text(
            "Recent Incidents",
            style = MaterialTheme.typography.titleMedium.copy(color = Black, fontSize = 20.sp)
        )
        SmallSpacer()

        //listview
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(detailedIncidents) { item ->
                CardRowView(item) {
                    appNavigator.openIncidentDetailsScreen(item)
                }
            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardRowView(item: GetDetailedIncident, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 2.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.incidentType + " | " + item.casualties,
                    style = TextStyle.Default.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Text(
                    item.date.formatDateTimeForDisplay(),
                    style = TextStyle.Default.copy(fontSize = 14.sp)
                )
                Text(
                    item.primaryText,
                    style = TextStyle.Default.copy(fontSize = 14.sp)
                )
            }

        }
    }
}

@Composable
fun CardView(modifier: Modifier, cardColor: CardColors, title: String, data: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = cardColor,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            SmallSpacer()
            Text(
                style = TextStyle(fontSize = 15.sp),
                text = data
            )
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeViewScreen()
//}