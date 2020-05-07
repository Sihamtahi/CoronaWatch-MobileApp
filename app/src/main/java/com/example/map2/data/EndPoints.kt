package com.example.map2.data

interface TmdbEndpoints {

    /*@GET("/todos")
    fun getList(@Query("api_key") key: String): Call<MyList>*/
    //@GET("todos")
    //  fun getList(@Path("id") id: String): Call<MyList>

    @GET("todos")
    fun getCorrdinates(): Call<List<Coordinates>>
}