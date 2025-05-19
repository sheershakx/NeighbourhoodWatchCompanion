package com.srg.neighbourhoodwatchcompanion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class IncidentInfo(
    @SerialName("id") var id: String? = null,
    @SerialName("user_id") var userId: String? = null,
    @SerialName("type") var type: String,
    @SerialName("title") var title: String,
    @SerialName("description") var description: String,
    @SerialName("date") var date: String,
    @SerialName("casualties") var casualties: String? = null,
    @SerialName("created_at") var createdAt: String? = null,
    @SerialName("updated_at") var updatedAt: String? = null
)