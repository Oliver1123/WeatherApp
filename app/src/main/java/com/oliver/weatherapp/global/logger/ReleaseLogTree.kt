package com.oliver.weatherapp.global.logger

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class ReleaseLogTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.ERROR -> {
                if (t == null)
                    Crashlytics.logException(Exception(message))
                else
                    Crashlytics.logException(t)
            }
        }
    }
}