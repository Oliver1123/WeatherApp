package com.oliver.weatherapp.screens


enum class DataState { LOADING, SUCCESS, ERROR }

data class Data<out T> constructor(
    val state: DataState,
    val data: T? = null,
    val message: String? = null
)