package com.aiinty.weatherly.api

import com.aiinty.weatherly.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val weatherApi = getInstance().create(WeatherAPI::class.java)

    private fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}