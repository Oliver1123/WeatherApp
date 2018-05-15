package com.oliver.weatherapp.global.di


import com.oliver.weatherapp.data.di.LocalModule
import com.oliver.weatherapp.data.di.NetworkModule
import com.oliver.weatherapp.data.di.RepositoryModule
import com.oliver.weatherapp.global.App
import com.oliver.weatherapp.screens.forecast.WeatherFragment
import com.oliver.weatherapp.screens.home.CitiesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,

    LocalModule::class,
    NetworkModule::class,

    RepositoryModule::class,

    ViewModelModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(fragment: CitiesFragment)

    fun inject(fragment: WeatherFragment)
}
