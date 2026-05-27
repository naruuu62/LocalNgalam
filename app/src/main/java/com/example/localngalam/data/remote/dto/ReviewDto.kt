package com.example.localngalam.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("destination_id") val destinationId: String = "",
    @SerializedName("user_id") val userId: String = "",
    @SerializedName("user_name") val userName: String = "",
    @SerializedName("rating") val rating: Int = 0,
    @SerializedName("comment") val comment: String = "",
    @SerializedName("created_at") val createdAt: String = ""
)

data class CreateReviewRequest(
    @SerializedName("destination_id") val destinationId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String
)

data class UpdateReviewRequest(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String
)
