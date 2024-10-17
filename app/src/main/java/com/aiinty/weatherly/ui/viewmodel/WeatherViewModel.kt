package com.aiinty.weatherly.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiinty.weatherly.BuildConfig
import com.aiinty.weatherly.api.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi

    fun getWeather(location: String) {
        viewModelScope.launch {
            val response = weatherApi.getWeather(BuildConfig.API_KEY, location)
            if (response.isSuccessful) {
                Log.i("WeatherViewModel", response.body().toString())
            } else {

            }
        }
    }

}