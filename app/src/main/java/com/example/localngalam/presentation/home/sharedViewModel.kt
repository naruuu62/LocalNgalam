package com.example.localngalam.presentation.home

import Tempat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class sharedViewModel : ViewModel() {
    private val _selectedTempat = MutableStateFlow<Tempat?>(null)
    val selectedTempat: StateFlow<Tempat?> get() = _selectedTempat

    fun setSelectedTempat(tempat: Tempat) {
        _selectedTempat.value = tempat
    }
}