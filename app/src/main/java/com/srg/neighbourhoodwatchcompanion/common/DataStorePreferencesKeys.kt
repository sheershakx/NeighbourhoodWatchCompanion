package com.srg.neighbourhoodwatchcompanion.common

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStorePreferencesKeys {
    val FIRST_NAME = stringPreferencesKey("first_name")
    val LAST_NAME = stringPreferencesKey("last_name")
    val EMAIL = stringPreferencesKey("email")
    val MOBILE = stringPreferencesKey("mobile")
    val USER_ID = stringPreferencesKey("user_id")
    val PROFILE_IMAGE = stringPreferencesKey("profile_image")

    //
    val USER_LATITUDE = stringPreferencesKey("user_latitude")
    val USER_LONGITUDE = stringPreferencesKey("user_longitude")

    val NEIGHBOURHOOD = stringPreferencesKey("neighbourhood")
}