package com.srg.neighbourhoodwatchcompanion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncidentLocationInformation(
    @SerialName("place_id") val placeId: String,
    @SerialName("incident_id") val incidentId: String?=null,
    @SerialName("lat") val lat: Double,
    @SerialName("lng") val lng: Double,
    @SerialName("primary_text") val primaryText: String,
    @SerialName("secondary_text") val secondaryText: String
) {
    companion object {
        fun empty() = IncidentLocationInformation(
            placeId = "",
            incidentId = "",
            lat = 0.0,
            lng = 0.0,
            primaryText = "",
            secondaryText = ""
        )
    }
}