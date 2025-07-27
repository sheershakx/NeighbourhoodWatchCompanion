package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident_details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.MediumSpacer
import com.srg.neighbourhoodwatchcompanion.common.XLSpacer
import com.srg.neighbourhoodwatchcompanion.common.formatDateTimeForDisplay
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident


@RequiresApi(Build.VERSION_CODES.O)
@BottomNavGraph
@Composable
fun IncidentDetailsScreen(
    incident: GetDetailedIncident,
    viewModel: IncidentDetailsViewModel = hiltViewModel()
) {
    val imageUrls by viewModel.imageUrls.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getSignedUrls(incident.incidentId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        // Incident Type
        Text("Incident Type", style = MaterialTheme.typography.titleMedium)
        MediumSpacer()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Incident Type",
                tint = Color(0xFF1E40AF), // Deep blue
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(incident.incidentType, style = MaterialTheme.typography.bodyMedium)
        }

        XLSpacer()

        // Title
        Text("Title", style = MaterialTheme.typography.titleMedium)
        MediumSpacer()
        Text(
            text = incident.title,
            style = MaterialTheme.typography.bodyMedium
        )

        XLSpacer()

        // Date & Time
        Text("Date & Time", style = MaterialTheme.typography.titleMedium)
        MediumSpacer()
        Text(incident.date.formatDateTimeForDisplay(), style = MaterialTheme.typography.bodyMedium)

        XLSpacer()

        // Location
        Text("Location", style = MaterialTheme.typography.titleMedium)
        MediumSpacer()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Location",
                tint = Color(0xFF1E40AF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                incident.primaryText + " | " + incident.secondaryText,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        XLSpacer()
        // Description
        Text("Description", style = MaterialTheme.typography.titleMedium)
        MediumSpacer()
        Text(
            text = incident.description,
            style = MaterialTheme.typography.bodyMedium
        )

        XLSpacer()
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

        XLSpacer()
        // Casualties
        Text("Casualties", style = MaterialTheme.typography.titleMedium)
        MediumSpacer()
        Text(text = incident.casualties, style = MaterialTheme.typography.bodyMedium)

    }
}

