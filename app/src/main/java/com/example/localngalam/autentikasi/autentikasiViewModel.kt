package com.example.localngalam.autentikasi

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.repository.AuthRepository
import com.example.localngalam.model.UserData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class autentikasiViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val authRepository = AuthRepository(sessionManager)

    // ─── State ────────────────────────────────────────────────────────────────

    private val _loginState = MutableStateFlow<Boolean?>(null)
    val loginState: StateFlow<Boolean?> get() = _loginState

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> get() = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // ─── Google Sign-In client (kept for Google OAuth) ─────────────────────

    lateinit var googleSignInClient: GoogleSignInClient

    fun initGoogleSignInClient(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("497053976903-cnniekv6vj70m4n04n9a50v31rttq0og.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    // ─── Auth Methods ─────────────────────────────────────────────────────────

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = { userData ->
                    _userData.value = userData
                    _loginState.value = true
                    Log.d("autentikasiViewModel", "Login berhasil: ${userData.email}")
                },
                onFailure = { error ->
                    _loginState.value = false
                    Log.e("autentikasiViewModel", "Login gagal: ${error.message}")
                }
            )
            _isLoading.value = false
        }
    }

    fun register(email: String, password: String, namaLengkap: String, noTelepon: String, avatarUri: android.net.Uri? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            
            var avatarBytes: ByteArray? = null
            if (avatarUri != null) {
                try {
                    val inputStream = getApplication<Application>().contentResolver.openInputStream(avatarUri)
                    avatarBytes = inputStream?.readBytes()
                    inputStream?.close()
                } catch (e: Exception) {
                    Log.e("autentikasiViewModel", "Gagal membaca foto profil", e)
                }
            }

            val result = authRepository.register(email, password, namaLengkap, noTelepon, avatarBytes)
            result.fold(
                onSuccess = { userData ->
                    _userData.value = userData
                    _loginState.value = true
                    Log.d("autentikasiViewModel", "Register berhasil: ${userData.email}")
                },
                onFailure = { error ->
                    _loginState.value = false
                    Log.e("autentikasiViewModel", "Register gagal: ${error.message}")
                }
            )
            _isLoading.value = false
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.signInWithGoogle(idToken)
            result.fold(
                onSuccess = { userData ->
                    _userData.value = userData
                    _loginState.value = true
                    Log.d("autentikasiViewModel", "Google Sign-In berhasil: ${userData.email}")
                },
                onFailure = { error ->
                    _loginState.value = false
                    Log.e("autentikasiViewModel", "Google Sign-In gagal: ${error.message}")
                }
            )
            _isLoading.value = false
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            authRepository.sendPasswordResetEmail(email)
        }
    }

    fun isUserLoggedIn(): Boolean = authRepository.isLoggedIn()

    fun signOut() {
        authRepository.signOut()
        _loginState.value = false
        _userData.value = null
        // Sign out from Google as well
        if (::googleSignInClient.isInitialized) {
            googleSignInClient.signOut()
        }
        Log.d("autentikasiViewModel", "Logout berhasil")
    }

    val userId: String get() = sessionManager.getUserId() ?: ""
}
