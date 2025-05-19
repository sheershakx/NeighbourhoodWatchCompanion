package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.compose.autocomplete.components.PlacesAutocompleteTextField
import com.google.android.libraries.places.compose.autocomplete.models.AutocompletePlace
import com.google.android.libraries.places.compose.autocomplete.models.toPlaceDetails
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph

@BottomNavGraph
@Composable
fun LocationInputScreen(
    predictions: List<AutocompletePrediction>,
    locationSearchText: Pair<String, String>,
    previewSelectedLocation: AutocompletePlace?,
    onLocationSearchUpdated: (String) -> Unit,
    onLocationSelected: (AutocompletePlace) -> Unit,
    onLocationInformationSaved: () -> Unit,
) {


    val zoomState by remember { mutableFloatStateOf(12f) }
    val markerState = rememberMarkerState()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(previewSelectedLocation) {
        previewSelectedLocation?.let {
            val selectedLatLng = it.latLng ?: LatLng(0.0, 0.0)
            markerState.position = selectedLatLng
            cameraPositionState.apply {
                animate(CameraUpdateFactory.newLatLngZoom(selectedLatLng, zoomState), 400)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlacesAutocompleteTextField(
            searchText = locationSearchText.first,
            predictions = predictions.map {
                it.toPlaceDetails()
            },
            onQueryChanged = {
                onLocationSearchUpdated(it)
            },
            onSelected = onLocationSelected,
            placeHolderText = "Enter location or intersection",
            onBackClicked = {

            }
        )
        if (previewSelectedLocation != null) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)

            ) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .shadow(elevation = 10.dp),
                    cameraPositionState = cameraPositionState
                ) {

                    Marker(
                        state = markerState,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                        draggable = false,
                        rotation = 20f
                    )
                }

            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            enabled = previewSelectedLocation != null,
            onClick = {
                onLocationInformationSaved()
            }
        ) {
            Text("Select location")
        }
    }
}
