package com.example.localngalam.presentation.home

import Tempat
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.repository.TempatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class homeViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val tempatRepository = TempatRepository(sessionManager)
    private val userRepository = com.example.localngalam.data.repository.UserRepository(sessionManager)

    private val _tempatPerKategori = MutableStateFlow<Map<String, List<Tempat>>>(emptyMap())
    val tempatPerKategori: StateFlow<Map<String, List<Tempat>>> get() = _tempatPerKategori
    
    private val _userData = MutableStateFlow<com.example.localngalam.model.UserData?>(null)
    val userData: StateFlow<com.example.localngalam.model.UserData?> get() = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val kategoriList = listOf("Restoran", "Alam", "Cafe")

    init {
        getTempatPerKategori()
        fetchUserData()
    }

    private fun fetchUserData() {
        val uid = sessionManager.getUserId()
        if (uid != null) {
            viewModelScope.launch {
                val user = userRepository.getUserById(uid)
                _userData.value = user
            }
        }
    }

    fun getTempatPerKategori() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = tempatRepository.getTempatPerKategori(kategoriList)
                _tempatPerKategori.value = result
            } catch (e: Exception) {
                Log.e("homeViewModel", "Gagal mengambil tempat per kategori: ${e.message}", e)
            }
            _isLoading.value = false
        }
    }

    fun getTempatById(tempatId: String, onResult: (Tempat?) -> Unit) {
        viewModelScope.launch {
            val tempat = tempatRepository.getTempatById(tempatId)
            onResult(tempat)
        }
    }

    fun getHarga(harga: Long): String {
        return when (harga) {
            1L -> "Rp. 1 - 25.000"
            2L -> "Rp. 25.000 - 50.000"
            3L -> "Rp. 50.000 - 100.000"
            4L -> "Rp. 100.000 - 200.000"
            5L -> "Rp. 200.000 - 500.000"
            else -> "tidak ada"
        }
    }
}
