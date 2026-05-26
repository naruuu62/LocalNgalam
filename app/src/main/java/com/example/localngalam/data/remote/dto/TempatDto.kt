package com.example.localngalam.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for the `tempat` table in Supabase.
 * Field names match Supabase column names (snake_case).
 */
data class TempatDto(

    @SerializedName("id")
    val id: String = "",

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("close")
    val close: String? = null,

    @SerializedName("deskripsi")
    val deskripsi: String? = null,

    @SerializedName("open")
    val open: String? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("price_range")
    val priceRange: Int? = null,

    @SerializedName("tags")
    val tags: List<Int>? = null,

    @SerializedName("gambar")
    val gambar: String? = null,

    @SerializedName("nama_lokasi")
    val namaLokasi: String? = null,

    @SerializedName("website")
    val website: String? = null,

    @SerializedName("deskripsi_ringkas")
    val deskripsiRingkas: String? = null
)