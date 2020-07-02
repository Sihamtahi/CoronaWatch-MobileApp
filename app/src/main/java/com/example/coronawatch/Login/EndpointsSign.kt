package com.example.coronawatch.Login


import retrofit2.Call
import retrofit2.http.*


interface APIServiceSign  {
    @Headers("Authorization: token ee5f6766123e0fa438f03380f300a8f74f081c9f")
    @POST("corona-watch-api/v1/users/new-user")
    fun SignUp(@Body User :user  ): Call<user?>?

    @Headers("Authorization: token ee5f6766123e0fa438f03380f300a8f74f081c9f")
    @POST("corona-watch-api/v1/users/api-token-auth")
    fun SignIn(@Body User :AuthUser): Call<AuthUser?>?

    @GET("corona-watch-api/v1/users/{username}")
    fun getUserInfo(@Header("Authorization") authHeader:String ,@Path("username") username:String): Call<user?>?


}
