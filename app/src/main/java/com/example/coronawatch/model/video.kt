package com.example.coronawatch.model



import com.google.gson.annotations.SerializedName
data class Video(
    @SerializedName("id")
    val id : Int,
    @SerializedName("video_embed_url")
    val video_embed_url : String,
    @SerializedName("video_id")
    val video_id : String ,
    @SerializedName("published_at")
    val published_at :String,
    @SerializedName("title")
    val title : String ,
    @SerializedName("channel_title")
    val channel_title :String ,
    @SerializedName("description")
    val  description : String,
    @SerializedName("is_validated")
    val is_validated : String,
    @SerializedName("date")
    val date : String
)