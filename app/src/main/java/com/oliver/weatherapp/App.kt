package com.oliver.weatherapp


import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

public class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    private fun initTimber() {
        Timber.plant(DebugTree())
    }
}