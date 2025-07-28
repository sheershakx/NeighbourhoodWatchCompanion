package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident_details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.BuildConfig
import com.srg.neighbourhoodwatchcompanion.common.LargeSpacer
import com.srg.neighbourhoodwatchcompanion.common.MediumSpacer
import com.srg.neighbourhoodwatchcompanion.common.SmallSpacer
import com.srg.neighbourhoodwatchcompanion.common.formatDateTimeForDisplay
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Purple40
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Purple80
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Purple90
import com.srg.neighbourhoodwatchcompanion.presenter.theme.WarningColor


@RequiresApi(Build.VERSION_CODES.O)
@BottomNavGraph
@Composable
fun IncidentDetailsScreen(
    incident: GetDetailedIncident,
    viewModel: IncidentDetailsViewModel = hiltViewModel()
) {
    val imageUrls by viewModel.imageUrls.collectAsState()

    val mapUrl = remember(incident.lat, incident.lng) {
        "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=${incident.lat},${incident.lng}" +
                "&zoom=19" +
                "&size=600x400" +
                "&markers=color:red%7Clabel:C%7C${incident.lat},${incident.lng}" +
                "&key=${BuildConfig.PLACES_KEY}"
    }

    LaunchedEffect(Unit) {
        viewModel.getSignedUrls(incident.incidentId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        //Card wrap for location and map sc
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(containerColor = Purple90),
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row {
                    Icon(
                        Icons.Default.Warning,
                        "Incident type icon",
                        tint = WarningColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Column(
                        modifier = Modifier.padding(start = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            incident.incidentType,
                            style = TextStyle.Default.copy(
                                fontSize = 22.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            incident.date.formatDateTimeForDisplay(),
                            style = TextStyle.Default.copy(
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
                SmallSpacer()
                Box {
                    AsyncImage(
                        model = mapUrl,
                        contentDescription = "Incident image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(color = Purple40)
                                .border(1.dp, Purple80, RoundedCornerShape(12.dp))
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                            text = incident.primaryText,
                            style = TextStyle.Default.copy(fontSize = 18.sp, color = Color.White)
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            text = incident.secondaryText,
                            textAlign = TextAlign.Center,
                            style = TextStyle.Default.copy(
                                fontSize = 18.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
        MediumSpacer()
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(containerColor = Purple90),
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                MediumSpacer()
                // Title
                Text("Title", style = MaterialTheme.typography.titleMedium)
                MediumSpacer()
                Text(
                    text = incident.title,
                    style = MaterialTheme.typography.bodyMedium
                )

                LargeSpacer()
                // Description
                Text("Description", style = MaterialTheme.typography.titleMedium)
                MediumSpacer()
                Text(
                    text = incident.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (imageUrls.isNotEmpty()) {
                    LargeSpacer()
                    // Images
                    Text("Images", style = MaterialTheme.typography.titleMedium)
                    MediumSpacer()
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(imageUrls) { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Incident image",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                LargeSpacer()
                // Casualties
                Text("Casualties", style = MaterialTheme.typography.titleMedium)
                MediumSpacer()
                Text(text = incident.casualties, style = MaterialTheme.typography.bodyMedium)

            }
        }
    }
}

