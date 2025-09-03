package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.PermissionHelper
import com.srg.neighbourhoodwatchcompanion.common.SmallSpacer
import com.srg.neighbourhoodwatchcompanion.common.StringResources.DASHBOARD_MESSAGE
import com.srg.neighbourhoodwatchcompanion.common.XSmallSpacer
import com.srg.neighbourhoodwatchcompanion.common.formatDateTimeForDisplay
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.presenter.theme.colors
import com.srg.neighbourhoodwatchcompanion.presenter.theme.typo
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
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
    val activity = LocalActivity.current
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    var locationPermissionRequested by rememberSaveable { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf(Pair(0.0, 0.0)) }
    val defaultCardColor = CardColors(
        containerColor = Color.LightGray,
        contentColor = Color.Black,
        disabledContainerColor = Color.LightGray,
        disabledContentColor = Color.Black
    )

    LaunchedEffect(Unit) {
        PermissionHelper.handleLocationPermissionRequest(
            locationPermissionsState,
            locationPermissionRequested,
            { locationPermissionRequested = it },
            context
        ) { lat, lng ->
            currentLocation = Pair(lat, lng)
            Timber.tag("Current Location").d("Approx: $lat, $lng")
        }
    }

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
            .padding(horizontal = 10.dp)

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
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                )
                Text(
                    text = DASHBOARD_MESSAGE,
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(fontSize = 14.sp, fontStyle = FontStyle.Italic),
                    color = colors.onSurfaceVariant
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                Icons.Default.LocationOn, "Location Icon"
            )
            Text(currentLocation.first.toString())
            IconButton(
                {
                    // on click //
                }
            ) {
                Icon(Icons.Default.Refresh, "Refresh Location")
            }
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
                defaultCardColor.copy(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                "Incident this week",
                "9"
            )
            CardView(
                Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp),
                defaultCardColor.copy(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                "Total Incidents", "23"
            )
        }


        //Incident report action button

//        Button(modifier = Modifier
//            .align(Alignment.CenterHorizontally)
//            .height(IntrinsicSize.Min),
//            colors = ButtonDefaults.buttonColors(),
//            elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp),
//            border = BorderStroke(
//                width = 1.dp, brush = Brush.horizontalGradient(
//                    listOf(
//                        Purple40,
//                        WarningColor, YellowCard, Pink40
//                    )
//                )
//            ),
//            shape = RoundedCornerShape(60),
//            onClick = {
//                viewModel.onTriggerEvent(HomeEvent.IncidentFormButtonClicked)
//            }) {
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(6.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(Icons.Filled.AddCircle, null, modifier = Modifier.size(18.dp))
//                Text(
//                    "Report Incident",
//                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                )
//
//
//            }
//        }
        SmallSpacer()

        Text(
            "Recent Incidents",
            style = typo.titleMedium.copy(fontSize = 20.sp, color = colors.onSurface)
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
        colors = CardDefaults.cardColors(containerColor = colors.background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(Icons.Default.PlayArrow, "")

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    item.incidentType + " | " + item.casualties,
                    style = typo.titleMedium.copy(color = colors.onSurface)
                )
                XSmallSpacer()
                Text(
                    item.date.formatDateTimeForDisplay(),
                    style = typo.bodyMedium.copy(color = colors.onSurfaceVariant)
                )
                XSmallSpacer()
                Text(
                    item.primaryText,
                    style = typo.bodyMedium.copy(color = colors.onSurfaceVariant)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = typo.titleMedium.copy(fontWeight = FontWeight.Normal),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            SmallSpacer()
            Text(
                style = typo.titleLarge,
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