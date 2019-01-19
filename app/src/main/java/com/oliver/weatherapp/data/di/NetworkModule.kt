package com.oliver.weatherapp.data.di

import com.oliver.weatherapp.BuildConfig
import com.oliver.weatherapp.data.remote.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { getClient() }
    single { provideRetrofit(get()) }
    single { provideWeatherApi(get()) }
}

fun getClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()

    // Can be Level.BASIC, Level.HEADERS, or Level.BODY
    httpLoggingInterceptor.level = if (BuildConfig.DEBUG)
        HttpLoggingInterceptor.Level.BASIC
//            HttpLoggingInterceptor.Level.BODY
    else
        HttpLoggingInterceptor.Level.NONE

    val builder = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor(httpLoggingInterceptor)

    return builder.build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

//////////////////////////////////////////////////////////////////////////////////////////////////

fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
    return retrofit.create(WeatherApi::class.java)
}