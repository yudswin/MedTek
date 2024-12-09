package com.medtek.main.data.remote.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private const val BASE_URL =
        "based"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>): T = retrofit.create(service)

//    val weatherApi: WeatherApi by lazy {
//        retrofit.create(WeatherApi::class.java)
//    }
//
//    val quoteApi: QuoteApi by lazy {
//        retrofit.create(QuoteApi::class.java)
//    }
}