package com.example.signaler

import com.google.gson.annotations.SerializedName


data class suspected (
     @SerializedName("attachment")
     var attachmentx: Attachment,
     @SerializedName("date")
    var date: String,
     @SerializedName("is_treated")
     var is_treated: Boolean,
     @SerializedName("town")
    var town: Int,
     @SerializedName("users")
    var users:  ArrayList<Int>
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


