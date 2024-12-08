package com.srg.neighbourhoodwatchcompanion.app

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber


class TimberTreeInitializer : Timber.Tree(){

    init {
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = true
    }
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        if (priority == Log.ERROR) {
            //only log errors to crashlytics
            val exception = t ?: Exception(message)
            Firebase.crashlytics.log(message)
            Firebase.crashlytics.setCustomKey(tag.toString(), message)
            Firebase.crashlytics.recordException(exception)
        }
    }

}