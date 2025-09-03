package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident_details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.BuildConfig
import com.srg.neighbourhoodwatchcompanion.R
import com.srg.neighbourhoodwatchcompanion.common.LargeSpacer
import com.srg.neighbourhoodwatchcompanion.common.MediumSpacer
import com.srg.neighbourhoodwatchcompanion.common.SmallSpacer
import com.srg.neighbourhoodwatchcompanion.common.XSmallSpacer
import com.srg.neighbourhoodwatchcompanion.common.formatDateTimeForDisplay
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.presenter.theme.colors
import com.srg.neighbourhoodwatchcompanion.presenter.theme.typo


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
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        //Card wrap for location and map sc
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        incident.incidentType,
                        style = typo.titleLarge.copy(color = colors.onSurface)
                    )
                    Text(
                        incident.date.formatDateTimeForDisplay(),
                        style = typo.bodyMedium.copy(color = colors.onSurfaceVariant)
                    )
                }

                SmallSpacer()
                AsyncImage(
                    model = mapUrl,
                    contentDescription = "Incident location snapshot",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                MediumSpacer()
                Text(
                    textAlign = TextAlign.Start,
                    text = incident.primaryText,
                    style = typo.titleMedium.copy(color = colors.onSurface)
                )
                Text(
                    text = incident.secondaryText,
                    textAlign = TextAlign.Start,
                    style = typo.bodyMedium.copy(
                        color = colors.onSurfaceVariant
                    )
                )


            }
        }
        MediumSpacer()
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                MediumSpacer()
                // Title
                Text("Title", style = typo.titleMedium)
                XSmallSpacer()
                Text(
                    text = incident.title,
                    style = typo.titleLarge.copy(color = colors.onSurface)
                )
                LargeSpacer()
                // Description
                Text("Description", style = typo.titleMedium)
                XSmallSpacer()
                Text(
                    text = incident.description,
                    style = typo.bodyMedium.copy(color = colors.onSurfaceVariant)
                )

                if (imageUrls.isNotEmpty()) {
                    LargeSpacer()
                    // Images
                    Text("Images (${imageUrls.size})", style = typo.titleMedium)
                    SmallSpacer()
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        painter = painterResource(id = R.drawable.ic_casualties),
                        contentDescription = "casualties icon",
                    )
                    Text(
                        text = incident.casualties,
                        style = typo.bodyMedium.copy(color = colors.onSurfaceVariant)
                    )
                }


            }
        }
    }
}

