package com.example.localngalam.presentation.search

import Tempat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _tempatList = MutableLiveData<List<Tempat>>()
    val tempatList: LiveData<List<Tempat>> = _tempatList

    fun getTempatFilter(category: String) {
        val query = if (category == "All") db.collection("tempat")
        else db.collection("tempat").whereEqualTo("Category", category)

        query.get().addOnSuccessListener { snapshot ->
            viewModelScope.launch {
                val tempatData = snapshot?.map { queryDocumentSnapshot ->
                    val rawTags = queryDocumentSnapshot.get("Tag") as? List<*> ?: emptyList<String>()
                    val convertedTags = rawTags.mapNotNull { (it as? Number)?.toLong() }
                        .map { getTagNilai(it) }

                    Tempat(
                        id = queryDocumentSnapshot.id,
                        address = queryDocumentSnapshot.getString("Addres") ?: "",
                        category = queryDocumentSnapshot.getString("Category") ?: "",
                        close = queryDocumentSnapshot.getString("Close") ?: "",
                        deskripsi = queryDocumentSnapshot.getString("Deskripsi") ?: "",
                        open = queryDocumentSnapshot.getString("Open") ?: "",
                        phoneNumber = queryDocumentSnapshot.getString("Phone Number") ?: "",
                        priceRange = queryDocumentSnapshot.getLong("Price Range") ?: 0,
                        tags = convertedTags,
                        gambar = queryDocumentSnapshot.getString("gambar") ?: ""
                    )
                } ?: emptyList()
                _tempatList.value = tempatData
            }
        }
    }

    fun getTagNilai(nilai: Long): String {
        return when (nilai) {
            1L -> "Estetis"
            2L -> "Pemandangan Indah"
            3L -> "Desain Simple"
            4L -> "Desain Modern"
            5L -> "Desain Industrial"
            6L -> "Desain Vinatage"
            7L -> "Desain Tropis"
            8L -> "Desain Brutalist"
            9L -> "Desain unik"
            10L -> "Outdoor"
            11L -> "Indoor"
            12L -> "Tempat Santai"
            13L -> "Tempat Kerja"
            14L -> "Cocok untuk Keluarga"
            15L -> "Specialty Coffee"
            16L -> "Berbasis Budaya"
            17L -> "Entertainment"
            18L -> "Culinary Destination"
            19L -> "Best at Night"
            20L -> "Best at Day"
            21L -> "Chain/Franchise"
            22L -> "Independent Business"
            else -> "tidak ada"


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
