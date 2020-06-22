package com.example.signaler

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory




interface APIService  {
    @Headers("Authorization: Basic YWRtaW46YWRtaW4=")
    @POST("corona-watch-api/v1/reports/suspected-cases/")
    fun uploadImageToApi(@Body suspect_cas :suspected  ): Call<suspected?>?

    //@Headers("Authorization: Basic YWRtaW46YWRtaW4=")
    //@POST("corona-watch-api/v1/feeds/videos/")
    //fun uploadVideoToApi(@Body video_cas :video  ): Call<video?>?
    @JvmSuppressWildcards
    @Multipart
    @Headers("api_key: 668362919461129")
    @POST("v1_1/hj48n3vai/upload")
    fun uploadVideoTest(
        @Part("upload_preset") upload_preset:RequestBody,
        @Part file: MultipartBody.Part
        ): Call<ServerResponse?>?

}

object AppConfig {

    var BASE_URL = "https://api.cloudinary.com/"

    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}