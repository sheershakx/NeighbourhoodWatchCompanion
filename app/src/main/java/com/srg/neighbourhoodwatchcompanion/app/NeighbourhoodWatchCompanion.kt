package com.srg.neighbourhoodwatchcompanion.app

import android.app.Application
import androidx.annotation.StringRes
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.srg.neighbourhoodwatchcompanion.BuildConfig
import com.srg.neighbourhoodwatchcompanion.common.StringResources
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NeighbourhoodWatchCompanion : Application() {

    override fun onCreate() {
        super.onCreate()
        val isDev = true
        if (isDev) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberTreeInitializer())
        }
    }

}