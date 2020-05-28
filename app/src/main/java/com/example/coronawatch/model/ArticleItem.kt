package com.example.articles.model


import com.google.gson.annotations.SerializedName

data class ArticleItem (
    @SerializedName("attachment")
    val attachment: Attachment,
    @SerializedName("content")
    val content: String,
    @SerializedName("editor")
    val editor: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("is_validated")
    val isValidated: Boolean,
    @SerializedName("publication_date")
    val publicationDate: String,
    @SerializedName("title")
    val title: String
)