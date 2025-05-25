package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.mapview

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.srg.neighbourhoodwatchcompanion.presenter.theme.WarningColor
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@BottomNavGraph
@Composable
fun MapViewScreen(
    viewModel: MapViewViewModel = hiltViewModel(),
    appNavigator: AppNavigator,
) {
    //vm data variables
    val detailedIncidents = viewModel.detailedIncidents.collectAsState()
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

    val incidentCardDataMapped =
        detailedIncidents.value.map {
            IncidentCardDataModel(
                incidentType = it.incidentType,
                location = it.primaryText,
                dateTime = it.date,
                casualties = it.casualties
            )
        }

    val pagerState = rememberPagerState {
        incidentCardDataMapped.size
    }

    LaunchedEffect(pagerState.currentPage, detailedIncidents.value.size) {
        if (detailedIncidents.value.isNotEmpty()) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        detailedIncidents.value[pagerState.currentPage].lat,
                        detailedIncidents.value[pagerState.currentPage].lng
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

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
            ) {
                detailedIncidents.value.forEachIndexed { currentPage, it ->
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
                    modifier = Modifier.graphicsLayer {
                        scaleX = 1f - 0.08f * pageOffset
                        scaleY = 1f - 0.15f * pageOffset
                    },
                    incidentType = incidentCardDataMapped[page].incidentType,
                    location = incidentCardDataMapped[page].location,
                    dateTime = incidentCardDataMapped[page].dateTime,
                    casualties = incidentCardDataMapped[page].casualties
                )
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            IncidentInfoRow(icon = Icons.Filled.Warning, label = "Incident", value = incidentType)
            IncidentInfoRow(icon = Icons.Default.LocationOn, label = "Location", value = location)
            IncidentInfoRow(
                icon = Icons.Default.DateRange,
                label = "Date & Time",
                value = dateTime.formatDateTimeForDisplay()
            )
            IncidentInfoRow(icon = Icons.Default.Person, label = "Casualties", value = casualties)
        }
    }
}

@Composable
fun IncidentInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = if (label == "Incident") WarningColor else MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
