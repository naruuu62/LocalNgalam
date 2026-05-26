package com.example.localngalam.data.repository

import Tempat
import android.util.Log
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.remote.RetrofitClient
import com.example.localngalam.data.remote.dto.TempatDto

private const val TAG = "TempatRepository"

class TempatRepository(private val sessionManager: SessionManager) {

    private fun api() = RetrofitClient.authenticated(
        sessionManager.getAccessToken() ?: ""
    )

    suspend fun getTempat(category: String? = null): List<Tempat> {
        return try {
            val categoryFilter = if (category != null && category != "All") "eq.$category" else null
            val response = api().getTempat(select = "*", category = categoryFilter)
            if (response.isSuccessful) {
                response.body()?.map { it.toModel() } ?: emptyList()
            } else {
                Log.e(TAG, "getTempat gagal: ${response.code()} ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTempat exception: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getTempatById(id: String): Tempat? {
        return try {
            val response = api().getTempatById(id = "eq.$id", select = "*")
            if (response.isSuccessful) {
                response.body()?.firstOrNull()?.toModel()
            } else {
                Log.e(TAG, "getTempatById gagal: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTempatById exception: ${e.message}", e)
            null
        }
    }

    suspend fun getTempatPerKategori(kategoriList: List<String>): Map<String, List<Tempat>> {
        val result = mutableMapOf<String, List<Tempat>>()
        for (kategori in kategoriList) {
            result[kategori] = getTempat(category = kategori)
        }
        return result
    }

    private fun TempatDto.toModel(): Tempat {
        return Tempat(
            id = id,
            namaLokasi = namaLokasi ?: "",
            address = address ?: "",
            category = category ?: "",
            close = close ?: "",
            deskripsi = deskripsi ?: "",
            open = open ?: "",
            phoneNumber = phoneNumber ?: "",
            priceRange = priceRange?.toLong() ?: 0L,
            tags = tags?.map { getTagNilai(it.toLong()) } ?: emptyList(),
            gambar = gambar ?: ""
        )
    }

    private fun getTagNilai(nilai: Long): String {
        return when (nilai) {
            1L -> "Estetis"
            2L -> "Pemandangan Indah"
            3L -> "Desain Simple"
            4L -> "Desain Modern"
            5L -> "Desain Industrial"
            6L -> "Desain Vintage"
            7L -> "Desain Tropis"
            8L -> "Desain Brutalist"
            9L -> "Desain Unik"
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
}
