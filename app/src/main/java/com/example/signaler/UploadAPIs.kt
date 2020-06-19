package com.example.signaler

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.Callback
import retrofit2.http.POST


interface APIService  {
    @Headers("Authorization: Basic YWRtaW46YWRtaW4=")
    @POST("corona-watch-api/v1/reports/suspected-cases/")
    fun uploadImageToApi(@Body suspect_cas :suspected  ): Call<suspected?>?

}