package com.example.coronawatch.Article


import com.example.model.CommentsItem
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @Headers("Authorization: token ee5f6766123e0fa438f03380f300a8f74f081c9f")
    @POST("corona-watch-api/v1/feeds/{id}/comments/")
    fun sendComment(@Body commentaire: CommentsItem,@Path("id") id:Int): Call<CommentsItem?>?

}