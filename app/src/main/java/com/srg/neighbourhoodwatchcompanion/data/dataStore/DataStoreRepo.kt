package com.srg.neighbourhoodwatchcompanion.data.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.srg.neighbourhoodwatchcompanion.common.DataStorePreferencesKeys
import com.srg.neighbourhoodwatchcompanion.data.model.UserInfo
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepo @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val supabaseAuth: Auth
) {
    val firstName = dataStore.data.map {
        it[DataStorePreferencesKeys.FIRST_NAME]
    }

    val lastName = dataStore.data.map {
        it[DataStorePreferencesKeys.LAST_NAME]
    }

    val email = dataStore.data.map {
        it[DataStorePreferencesKeys.EMAIL]
    }
    val mobile = dataStore.data.map {
        it[DataStorePreferencesKeys.MOBILE]
    }
    val userId = dataStore.data.map {
        it[DataStorePreferencesKeys.USER_ID]
    }

    suspend fun saveUserData(
        userInfo: UserInfo
    ) {
        dataStore.edit {
            it[DataStorePreferencesKeys.FIRST_NAME] = userInfo.firstName.toString()
            it[DataStorePreferencesKeys.LAST_NAME] = userInfo.lastName.toString()
            it[DataStorePreferencesKeys.EMAIL] = supabaseAuth.currentUserOrNull()?.email.toString()
            it[DataStorePreferencesKeys.MOBILE] = userInfo.mobile.toString()
            it[DataStorePreferencesKeys.USER_ID] = userInfo.userId.toString()
        }
    }

    suspend fun clearUser() {
        dataStore.edit { it.clear() }
    }

}