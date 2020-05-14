package com.example.map2.data

import retrofit2.Call
import retrofit2.http.GET

interface TmdbEndpoints {

    /*@GET("/todos")
    fun getList(@Query("api_key") key: String): Call<MyList>*/
    //@GET("todos")
    //  fun getList(@Path("id") id: String): Call<MyList>

    @GET("v2/locations")
    fun getCorrdinates(): Call<MyList>
}