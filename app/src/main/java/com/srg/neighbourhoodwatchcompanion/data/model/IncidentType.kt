package com.srg.neighbourhoodwatchcompanion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class IncidentType(
    @SerialName("id") var id: String,
    @SerialName("incident_type") var incidentType: String?,
    @SerialName("created_at") var createdAt: String?,
    @SerialName("updated_at") var updatedAt: String?
){
    companion object{
        fun empty()=IncidentType("","","","")
    }
}
