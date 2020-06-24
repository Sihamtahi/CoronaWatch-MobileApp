package com.example.coronawatch.Signaler
import com.google.gson.annotations.SerializedName

data class reponsecloud(
    @SerializedName("access_mode")
    val accessMode: String,
    @SerializedName("asset_id")
    val assetId: String,
    @SerializedName("bytes")
    val bytes: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("original_filename")
    val originalFilename: String,
    @SerializedName("placeholder")
    val placeholder: Boolean,
    @SerializedName("public_id")
    val publicId: String,
    @SerializedName("resource_type")
    val resourceType: String,
    @SerializedName("secure_url")
    val secureUrl: String,
    @SerializedName("signature")
    val signature: String,
    @SerializedName("tags")
    val tags: List<Any>,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("version_id")
    val versionId: String,
    @SerializedName("width")
    val width: Int
)