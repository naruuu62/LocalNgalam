package com.example.localngalam.presentation.profile

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.remote.RetrofitClient
import com.example.localngalam.data.remote.SupabaseConfig
import com.example.localngalam.data.repository.UserRepository
import com.example.localngalam.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val userRepository = UserRepository(sessionManager)

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    init {
        val userId = sessionManager.getUserId()
        if (!userId.isNullOrBlank()) {
            getUserData(userId)
        } else {
            Log.w("ProfileViewModel", "User belum login")
        }
    }

    fun getUserData(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val userData = userRepository.getUserById(userId)
            if (userData != null) {
                _userData.value = userData
                // Cache nama ke SessionManager untuk dipakai di review
                if (userData.namaLengkap.isNotBlank()) {
                    sessionManager.saveUserName(userData.namaLengkap)
                }
                Log.d("ProfileViewModel", "Data user ditemukan: ${userData.namaLengkap}")
            } else {
                _userData.value = UserData(
                    uid = userId,
                    email = sessionManager.getUserEmail() ?: "",
                    namaLengkap = "",
                    noTelepon = ""
                )
                Log.w("ProfileViewModel", "Data user tidak ditemukan di tabel users")
            }
            _isLoading.value = false
        }
    }

    fun updateProfile(namaLengkap: String, bio: String, onSuccess: () -> Unit) {
        val current = _userData.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val fields = mapOf(
                "nama_lengkap" to namaLengkap,
                "bio" to bio
            )
            val success = userRepository.updateUserFields(current.uid, fields)
            if (success) {
                val updated = current.copy(namaLengkap = namaLengkap, bio = bio)
                _userData.value = updated
                sessionManager.saveUserName(namaLengkap)
                _updateSuccess.value = true
                onSuccess()
            } else {
                _updateSuccess.value = false
            }
            _isLoading.value = false
        }
    }

    fun uploadAvatar(uri: Uri, onSuccess: () -> Unit) {
        val current = _userData.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val context = getApplication<Application>()
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: run {
                    _isLoading.value = false
                    return@launch
                }
                inputStream.close()

                val filename = "avatar_${current.uid}_${UUID.randomUUID()}.jpg"
                val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

                val api = RetrofitClient.authenticated(sessionManager.getAccessToken() ?: "")
                val response = api.uploadAvatar(filename, requestBody)

                if (response.isSuccessful) {
                    // Construct public URL (format Supabase Storage v1)
                    val baseUrl = SupabaseConfig.BASE_URL.trimEnd('/')
                    val publicUrl = "$baseUrl/storage/v1/object/public/avatars/$filename"

                    // Update user record with new avatar URL
                    val fields = mapOf(
                        "foto_profil" to publicUrl
                    )
                    val upsertSuccess = userRepository.updateUserFields(current.uid, fields)
                    if (upsertSuccess) {
                        val updated = current.copy(fotoProfil = publicUrl)
                        _userData.value = updated
                        onSuccess()
                    }
                } else {
                    Log.e("ProfileViewModel", "Upload gagal: ${response.code()} ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "uploadAvatar exception: ${e.message}", e)
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }
}
