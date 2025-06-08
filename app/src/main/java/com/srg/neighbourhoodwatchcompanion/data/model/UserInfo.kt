package com.srg.neighbourhoodwatchcompanion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserInfo(
    @SerialName("id") var id: String? = null,
    @SerialName("user_id") var userId: String? = null,
    @SerialName("first_name") var firstName: String? = null,
    @SerialName("last_name")
    var lastName: String? = null,
    @SerialName("mobile")
    var mobile: String? = null,

    @SerialName("image_path")
    var imagePath: String? = null,
    @SerialName("created_at")

    var createdAt: String? = null,
    @SerialName("updated_at")

    var updatedAt: String? = null

) {
    companion object {
        val empty = UserInfo("", "", "", "", "", "", "")

    }
}