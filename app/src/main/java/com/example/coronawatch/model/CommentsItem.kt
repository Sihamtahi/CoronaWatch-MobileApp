package com.example.model


import com.google.gson.annotations.SerializedName

data class CommentsItem(
    @SerializedName("comment_date")
    val commentDate: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("publication")
    val publication: Int,
    @SerializedName("user")
    val user: Int
)