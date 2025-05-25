package com.srg.neighbourhoodwatchcompanion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDetailedIncident(
    @SerialName("incident_id") val incidentId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("date") val date: String,
    @SerialName("casualties")  val casualties: String,
    @SerialName("incident_type")  val incidentType: String,
    @SerialName("lat")  val lat: Double,
    @SerialName("lng")  val lng: Double,
    @SerialName("primary_text")  val primaryText: String,
    @SerialName("secondary_text")  val secondaryText: String
)
