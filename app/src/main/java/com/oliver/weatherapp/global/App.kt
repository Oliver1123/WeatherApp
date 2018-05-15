package com.oliver.weatherapp.global


import android.app.Application

import com.oliver.weatherapp.global.di.AppComponent
import com.oliver.weatherapp.global.di.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

public class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    private fun initTimber() {
        Timber.plant(DebugTree())
    }
}