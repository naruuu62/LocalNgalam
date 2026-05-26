package com.example.localngalam.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BucketListDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("user_id") val userId: String = "",
    @SerializedName("destination_id") val destinationId: String = "",
    @SerializedName("created_at") val createdAt: String = ""
)

data class CreateBucketListRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("destination_id") val destinationId: String
)
