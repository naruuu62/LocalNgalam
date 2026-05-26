package com.example.localngalam.presentation.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.repository.UserRepository
import com.example.localngalam.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val userRepository = UserRepository(sessionManager)

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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

    fun logout() {
        sessionManager.clearSession()
    }
}
