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
