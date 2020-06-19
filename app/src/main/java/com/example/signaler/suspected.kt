package com.example.signaler

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.R.id


 data class suspected (
    @SerializedName("attachment")
     var attachment: Attachment,
    @SerializedName("date")
    var date: String,
    @SerializedName("is_treated")
     var is_treated: Boolean,
    @SerializedName("town")
    var town: Int,
    @SerializedName("users")
    var users:  ArrayList<Int>
)
