package com.example.coronawatch.Login

import com.example.signaler.Attachment
import com.google.gson.annotations.SerializedName

data class user (
    @SerializedName("username")
    var username: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("role")
    var role: String,
    @SerializedName("image_url")
    var image_url: String
)
data class AuthUser (
    @SerializedName("user_id")
    var user_id: Int,
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String
)

