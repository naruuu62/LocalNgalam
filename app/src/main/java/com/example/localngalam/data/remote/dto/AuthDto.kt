package com.example.localngalam.data.remote.dto

import com.google.gson.annotations.SerializedName

// ─── Auth Request Bodies ───────────────────────────────────────────────────

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("data") val data: UserMetadata? = null
)

data class UserMetadata(
    @SerializedName("nama_lengkap") val namaLengkap: String = "",
    @SerializedName("no_telepon") val noTelepon: String = ""
)

data class ResetPasswordRequest(
    @SerializedName("email") val email: String
)

data class GoogleSignInRequest(
    @SerializedName("provider") val provider: String = "google",
    @SerializedName("id_token") val idToken: String,
    @SerializedName("access_token") val accessToken: String? = null
)

// ─── Auth Response ─────────────────────────────────────────────────────────

data class AuthResponse(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("token_type") val tokenType: String? = null,
    @SerializedName("expires_in") val expiresIn: Int? = null,
    @SerializedName("user") val user: SupabaseUser? = null
)

data class SupabaseUser(
    @SerializedName("id") val id: String = "",
    @SerializedName("email") val email: String? = null,
    @SerializedName("user_metadata") val userMetadata: UserMetadataResponse? = null
)

data class UserMetadataResponse(
    @SerializedName("nama_lengkap") val namaLengkap: String? = null,
    @SerializedName("no_telepon") val noTelepon: String? = null,
    @SerializedName("full_name") val fullName: String? = null, // from Google OAuth
    @SerializedName("avatar_url") val avatarUrl: String? = null
)

data class AuthError(
    @SerializedName("error") val error: String? = null,
    @SerializedName("error_description") val errorDescription: String? = null,
    @SerializedName("msg") val msg: String? = null
)
