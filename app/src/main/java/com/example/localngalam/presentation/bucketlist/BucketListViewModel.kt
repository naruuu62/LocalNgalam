package com.example.localngalam.presentation.bucketlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.repository.BucketListItem
import com.example.localngalam.data.repository.BucketListRepository
import com.example.localngalam.data.repository.TempatRepository
import Tempat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BucketListViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val bucketListRepository = BucketListRepository(sessionManager)
    private val tempatRepository = TempatRepository(sessionManager)

    private val _bucketList = MutableStateFlow<List<BucketListItem>>(emptyList())
    val bucketList: StateFlow<List<BucketListItem>> = _bucketList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // destinationId -> namaLokasi, gambar cache
    private val _destinationNames = MutableStateFlow<Map<String, String>>(emptyMap())
    val destinationNames: StateFlow<Map<String, String>> = _destinationNames

    private val _destinationImages = MutableStateFlow<Map<String, String>>(emptyMap())
    val destinationImages: StateFlow<Map<String, String>> = _destinationImages

    private val _destinationTempat = MutableStateFlow<Map<String, Tempat>>(emptyMap())
    val destinationTempat: StateFlow<Map<String, Tempat>> = _destinationTempat

    init {
        fetchBucketList()
    }

    fun fetchBucketList() {
        viewModelScope.launch {
            _isLoading.value = true
            val list = bucketListRepository.getBucketList()
            _bucketList.value = list
            // Load detail setiap destination dari TempatRepository
            val namesMap = mutableMapOf<String, String>()
            val imagesMap = mutableMapOf<String, String>()
            val tempatMap = mutableMapOf<String, Tempat>()
            list.forEach { item ->
                val tempat = tempatRepository.getTempatById(item.destinationId)
                if (tempat != null) {
                    namesMap[item.destinationId] = tempat.namaLokasi.ifBlank { tempat.id }
                    imagesMap[item.destinationId] = tempat.gambar
                    tempatMap[item.destinationId] = tempat
                }
            }
            _destinationNames.value = namesMap
            _destinationImages.value = imagesMap
            _destinationTempat.value = tempatMap
            _isLoading.value = false
        }
    }

    fun removeFromBucketList(destinationId: String) {
        viewModelScope.launch {
            val success = bucketListRepository.deleteFromBucketList(destinationId)
            if (success) {
                _bucketList.value = _bucketList.value.filter { it.destinationId != destinationId }
            }
        }
    }
}
