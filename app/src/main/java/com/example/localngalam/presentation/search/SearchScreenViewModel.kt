package com.example.localngalam.presentation.search

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

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val tempatRepository = TempatRepository(sessionManager)

    private val _tempatList = MutableStateFlow<List<Tempat>>(emptyList())
    val tempatList: StateFlow<List<Tempat>> = _tempatList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getTempatFilter(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = tempatRepository.getTempat(category = category)
                _tempatList.value = result
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Gagal mengambil data tempat: ${e.message}", e)
            }
            _isLoading.value = false
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
