package com.example.localngalam.data.repository

import android.util.Log
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.remote.RetrofitClient
import com.example.localngalam.data.remote.dto.UpsertUserRequest
import com.example.localngalam.model.UserData

private const val TAG = "UserRepository"

class UserRepository(private val sessionManager: SessionManager) {

    private fun api() = RetrofitClient.authenticated(
        sessionManager.getAccessToken() ?: ""
    )

    suspend fun getUserById(uid: String): UserData? {
        return try {
            val response = api().getUserById(uid = "eq.$uid", select = "*")
            if (response.isSuccessful) {
                val dto = response.body()?.firstOrNull()
                dto?.let {
                    UserData(
                        uid = it.uid,
                        namaLengkap = it.namaLengkap,
                        noTelepon = it.noTelepon,
                        email = it.email,
                        fotoProfil = it.fotoProfil
                    )
                }
            } else {
                Log.e(TAG, "getUserById gagal: ${response.code()} ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getUserById exception: ${e.message}", e)
            null
        }
    }

    suspend fun upsertUser(userData: UserData) {
        try {
            val request = UpsertUserRequest(
                uid = userData.uid,
                namaLengkap = userData.namaLengkap,
                noTelepon = userData.noTelepon,
                email = userData.email,
                fotoProfil = userData.fotoProfil
            )
            val response = api().upsertUser(request)
            if (response.isSuccessful) {
                Log.d(TAG, "User berhasil di-upsert: ${userData.uid}")
            } else {
                Log.e(TAG, "upsertUser gagal: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "upsertUser exception: ${e.message}", e)
        }
    }
}
