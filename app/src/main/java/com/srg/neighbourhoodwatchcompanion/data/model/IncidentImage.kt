package com.srg.neighbourhoodwatchcompanion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class IncidentImage(
    @SerialName("id") var id: String?=null,
    @SerialName("incident_id") var incidentId: String,
    @SerialName("path") var path: String,
    @SerialName("created_at") var createdAt: String? = null,
)