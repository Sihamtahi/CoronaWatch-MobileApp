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

data class videoFeed (
    @SerializedName("attachment")
    var attachment: Attachment,
    @SerializedName("publication_date")
    var publication_date: String,
    @SerializedName("is_validated")
    var is_validated: Boolean,
    @SerializedName("is_deleted")
    var is_deleted: Boolean,
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("user")
    var user:  Int
)
data class Attachment(
    @SerializedName("extension")
    var extension: String,
    @SerializedName("file_url")
    var file_url: String,
    @SerializedName("nom")
    var nom: String,
    @SerializedName("file")
    var file: String,
    @SerializedName("date")
    var date: String
)