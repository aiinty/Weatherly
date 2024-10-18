package com.aiinty.weatherly.api

sealed class NetworkResponse<out T> {

    data class Success<out T>(val data: T) : NetworkResponse<T>()
    object Loading : NetworkResponse<Nothing>()
    data class Failure(val message: String) : NetworkResponse<Nothing>()

}