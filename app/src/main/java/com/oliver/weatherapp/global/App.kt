package com.oliver.weatherapp.global


import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.oliver.weatherapp.BuildConfig
import com.oliver.weatherapp.data.di.localModule
import com.oliver.weatherapp.data.di.networkModule
import com.oliver.weatherapp.data.di.repositoryModule
import com.oliver.weatherapp.global.di.viewModelModule
import com.oliver.weatherapp.global.logger.ReleaseLogTree
import com.oliver.weatherapp.utilsModule
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


public class App : Application() {


    override fun onCreate() {
        super.onCreate()

        initTimber()
        initCrashlytics()
        initKoin()
    }

    private fun initKoin() {
        startKoin(this, modules)
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

val modules = listOf(
    localModule,
    networkModule,
    repositoryModule,
    viewModelModule,

    utilsModule
)