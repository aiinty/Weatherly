package com.aiinty.weatherly.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiinty.weatherly.BuildConfig
import com.aiinty.weatherly.api.NetworkResponse
import com.aiinty.weatherly.api.RetrofitInstance
import com.aiinty.weatherly.api.weathermodel.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()

    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getWeather(location: String) {
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(BuildConfig.API_KEY, location)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Failure("Fail to load data")
                }
            } catch (e : Exception) {
                _weatherResult.value = NetworkResponse.Failure(e.message.toString())
            }
        }
    }

}