package com.example.coronawatch.Signaler

import com.example.signaler.suspected
import com.example.signaler.videoFeed
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface APIService  {
    @Headers("Authorization: token ee5f6766123e0fa438f03380f300a8f74f081c9f")
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


    @Headers("Authorization: token ee5f6766123e0fa438f03380f300a8f74f081c9f")
    @POST("corona-watch-api/v1/reports/suspected-cases/v2")
    fun uploadVideoToHeroku(@Body suspect_cas :suspected  ): Call<suspected?>?

    @Headers("Authorization: token ee5f6766123e0fa438f03380f300a8f74f081c9f")
    @POST("corona-watch-api/v1/feeds/videos/v2/")
    fun uploadVideoToApi(@Body video_cas : videoFeed): Call<videoFeed?>?

    @JvmSuppressWildcards
    @Multipart
    @Headers("api_key: 668362919461129")
    @POST("v1_1/hj48n3vai/upload")
    fun uploadVideoTest(
        @Part("upload_preset") upload_preset:RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ServerResponse?>?
}
