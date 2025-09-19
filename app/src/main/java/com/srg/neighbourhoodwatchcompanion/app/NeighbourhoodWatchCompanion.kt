package com.srg.neighbourhoodwatchcompanion.app

import android.app.Application
import com.srg.neighbourhoodwatchcompanion.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NeighbourhoodWatchCompanion : Application() {

    override fun onCreate() {
        super.onCreate()
        val isDev = BuildConfig.DEBUG
        if (isDev) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberTreeInitializer())
        }
    }

}