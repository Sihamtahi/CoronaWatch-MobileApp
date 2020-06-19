package com.example.signaler


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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