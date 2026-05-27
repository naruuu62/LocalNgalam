package com.example.localngalam.model

/**
 * Domain model for user profile data.
 * Firebase imports removed — persistence handled by UserRepository via Supabase.
 */
data class UserData(
    val namaLengkap: String = "",
    val noTelepon: String = "",
    val email: String = "",
    val uid: String = "",
    val fotoProfil: String? = null,
    val bio: String? = null
)