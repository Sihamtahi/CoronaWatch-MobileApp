package com.example.coronawatch.Signaler

import com.example.signaler.suspected
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface APIService  {
    @Headers("Authorization: Basic YWRtaW46YWRtaW4=")
    @POST("corona-watch-api/v1/reports/suspected-cases/")
    fun uploadImageToApi(@Body suspect_cas :suspected  ): Call<suspected?>?


    @JvmSuppressWildcards
    @Multipart
    @Headers("api_key: 668362919461129")
    @POST("v1_1/hj48n3vai/upload")
    fun uploadIMAGE(
        @Part("upload_preset") upload_preset:RequestBody,
        @Part file: MultipartBody.Part
    ): Call<reponsecloud?>?


}
