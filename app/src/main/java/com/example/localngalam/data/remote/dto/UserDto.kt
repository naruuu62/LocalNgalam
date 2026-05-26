package com.example.localngalam.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for the `users` table in Supabase.
 * Field names match the PostgreSQL column names.
 */
data class UserDto(
    @SerializedName("uid") val uid: String = "",
    @SerializedName("nama_lengkap") val namaLengkap: String = "",
    @SerializedName("no_telepon") val noTelepon: String = "",
    @SerializedName("email") val email: String = ""
)

/**
 * Request body for upsert (insert or update) user data.
 */
data class UpsertUserRequest(
    @SerializedName("uid") val uid: String,
    @SerializedName("nama_lengkap") val namaLengkap: String,
    @SerializedName("no_telepon") val noTelepon: String,
    @SerializedName("email") val email: String
)
