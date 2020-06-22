package com.example.signaler

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class video (
    @SerializedName("attachment")
    var attachment: Attachment,
    @SerializedName("publication_date")
    var date: String,
    @SerializedName("is_treated")
    var is_treated: Boolean,
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("user")
    var user:  Int
)

data class videoTest (
    @SerializedName("upload_preset")
    var upload_preset: String,
    @SerializedName("file")
    var file: String
)

class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("success")
    var success: Boolean = false
        internal set
    @SerializedName("message")
    var message: String? = null
        internal set

}