package com.vineet.clock

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IServiceFunctions {
    @GET("api/TimeZone/AvailableTimeZones")
    fun getAllTimeZones() : Call<List<String>>

    @GET("api/Time/current/zone")
    fun getTimeZone(@Query("timeZone") timeZone : String?) : Call<JsonElement>
}