package com.example.coronawatch


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface TmdbEndpoints {

    @GET("v2/locations")
    fun getCorrdinates(): Call<MyList>

    @GET("v2/latest")
    fun getLatestData(): Call<Map<String, latest>>

    @GET
    fun getlatestCountry(@Url url:String): Call<MyList>
}