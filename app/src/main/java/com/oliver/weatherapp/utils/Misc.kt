package com.oliver.weatherapp.utils

import android.app.Activity
import android.arch.lifecycle.*
import com.oliver.weatherapp.global.App
import com.oliver.weatherapp.global.di.AppComponent
import com.oliver.weatherapp.screens.base.BaseActivity
import com.oliver.weatherapp.screens.base.BaseFragment


val Activity.app: App get() = application as App

fun BaseActivity.getAppComponent(): AppComponent = (app).appComponent

fun BaseFragment.getAppComponent(): AppComponent = (activity as BaseActivity).getAppComponent()

inline fun <reified T : ViewModel> BaseActivity.getViewModel(viewModelFactory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}

inline fun <reified T : ViewModel> BaseFragment.getViewModel(viewModelFactory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}
