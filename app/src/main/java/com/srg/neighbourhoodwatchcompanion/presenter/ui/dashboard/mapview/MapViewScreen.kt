package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.mapview

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.formatDateTimeForDisplay
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Black
import com.srg.neighbourhoodwatchcompanion.presenter.theme.DangerColor
import com.srg.neighbourhoodwatchcompanion.presenter.theme.colors
import com.srg.neighbourhoodwatchcompanion.presenter.theme.typo
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@BottomNavGraph
@Composable
fun MapViewScreen(
    viewModel: MapViewViewModel = hiltViewModel(),
    appNavigator: AppNavigator,
) {
    //vm data variables
    val detailedIncidents by viewModel.detailedIncidents.collectAsState()
    val uiState by viewModel.uiState.collectAsState()


    //map variables
    val cameraPositionState = rememberCameraPositionState()
    val zoomState = remember { mutableFloatStateOf(15f) }
    val mapProperties = MapProperties(
        mapType = MapType.NORMAL,
    )
    val mapUiSettings = MapUiSettings(
        zoomControlsEnabled = true,
        compassEnabled = false,
        mapToolbarEnabled = false,
        myLocationButtonEnabled = true
    )


    val pagerState = rememberPagerState {
        detailedIncidents.size
    }

    LaunchedEffect(pagerState.currentPage, detailedIncidents.size) {
        if (detailedIncidents.isNotEmpty()) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        detailedIncidents[pagerState.currentPage].lat,
                        detailedIncidents[pagerState.currentPage].lng
                    ), zoomState.floatValue
                ), 800
            )
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is BaseViewState.Data -> {
                val mapViewState = (uiState.cast<BaseViewState.Data<MapViewState>>()).value
                mapViewState.scrollPageTo?.let {
                    pagerState.animateScrollToPage(it)
                }
            }

            else -> {}
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
            ) {
                detailedIncidents.forEachIndexed { currentPage, it ->
                    Circle(
                        center = LatLng(it.lat, it.lng),
                        radius = 500.0,
                        fillColor = DangerColor.copy(alpha = 0.3f),
                        strokeColor = Black,
                        strokeWidth = 1f,
                        clickable = true
                    ) {
                        viewModel.onTriggerEvent(MapViewEvent.MapMarkerClicked(currentPage))
                    }

                }
            }
            HorizontalPager(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                state = pagerState,
                pageSpacing = 5.dp,
                contentPadding = PaddingValues(horizontal = 35.dp)
            ) { page ->
                val pageOffset = (
                        (pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue
                IncidentCard(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = 1f - 0.08f * pageOffset
                            scaleY = 1f - 0.15f * pageOffset
                        }
                        .animateContentSize(),
                    incidentType = detailedIncidents[page].incidentType,
                    location = detailedIncidents[page].primaryText,
                    dateTime = detailedIncidents[page].date,
                    casualties = detailedIncidents[page].casualties,
                ) {
                    appNavigator.openIncidentDetailsScreen(detailedIncidents[page])
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncidentCard(
    incidentType: String,
    location: String,
    dateTime: String,
    casualties: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IncidentInfoRow(
                icon = Icons.Filled.Warning,
                value = incidentType
            )
            IncidentInfoRow(
                icon = Icons.Default.DateRange,
                value = dateTime.formatDateTimeForDisplay()
            )
            IncidentInfoRow(
                icon = Icons.Default.LocationOn,
                value = location
            )
            IncidentInfoRow(
                icon = Icons.Default.Person,
                value = casualties
            )
            IncidentInfoRow(
                icon = Icons.Default.Info,
                value = "description sample description sample description"
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                onClick = {
                    onClick()
                }) {
                Text("View Full Details", style = typo.labelSmall.copy(color = colors.onPrimary))
            }
        }
    }
}

@Composable
fun IncidentInfoRow(icon: ImageVector, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = typo.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

