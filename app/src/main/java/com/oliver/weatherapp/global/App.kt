package com.oliver.weatherapp.global


import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.oliver.weatherapp.BuildConfig
import com.oliver.weatherapp.global.di.AppComponent
import com.oliver.weatherapp.global.di.DaggerAppComponent
import com.oliver.weatherapp.global.logger.ReleaseLogTree
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import timber.log.Timber.DebugTree


public class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initCrashlytics()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())
        else
            Timber.plant(ReleaseLogTree())
    }

    private fun initCrashlytics() {
        // Set up Crashlytics, disabled for debug builds
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()

        Fabric.with(this, crashlyticsKit)
    }
}