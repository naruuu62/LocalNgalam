package com.example.localngalam.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for the `tempat` table in Supabase.
 * Field names match Supabase column names (snake_case).
 */
data class TempatDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("close") val close: String = "",
    @SerializedName("deskripsi") val deskripsi: String = "",
    @SerializedName("open") val open: String = "",
    @SerializedName("phone_number") val phoneNumber: String = "",
    @SerializedName("price_range") val priceRange: Int = 0,
    @SerializedName("tags") val tags: List<Int> = emptyList(),
    @SerializedName("gambar") val gambar: String = ""
)
