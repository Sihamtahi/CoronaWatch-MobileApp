package com.example.articles.model


import com.google.gson.annotations.SerializedName

data class Attachment(
    @SerializedName("date")
    val date: String,
    @SerializedName("extension")
    val extension: String,
    @SerializedName("file_url")
    val file_url: String,
    @SerializedName("file")
    val file: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nom")
    val nom: String
)