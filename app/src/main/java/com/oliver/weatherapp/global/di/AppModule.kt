package com.oliver.weatherapp.global.di

import android.content.Context
import com.oliver.weatherapp.global.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: App): Context {
        return application.applicationContext
    }
}
