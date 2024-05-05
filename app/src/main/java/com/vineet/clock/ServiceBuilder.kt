package com.vineet.clock

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val baseUrl = "https://timeapi.io/"

    //Create Http client


    //Create Retrofit builder

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttp = OkHttpClient.Builder().addInterceptor(logger)
    private val builder: Retrofit.Builder = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())


    //Create retrofit instance

    private val retrofit: Retrofit = builder.build()

    // Here we should pass the interface class
    fun <T> buildService(ServiceType: Class<T>): T {
        return retrofit.create(ServiceType)
    }
}