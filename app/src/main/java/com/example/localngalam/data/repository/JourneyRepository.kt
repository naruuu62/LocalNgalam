package com.example.localngalam.data.repository

import android.util.Log
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.remote.RetrofitClient
import com.example.localngalam.data.remote.dto.CreateJourneyRequest
import com.example.localngalam.data.remote.dto.JourneyDto
import com.example.localngalam.data.remote.dto.UpdateDaftarPerjalananRequest
import com.example.localngalam.data.remote.dto.UpdateTipePerjalananRequest
import com.example.localngalam.data.remote.dto.toDto
import com.example.localngalam.data.remote.dto.toModel
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.model.tempatPerjalanan

private const val TAG = "JourneyRepository"

class JourneyRepository(private val sessionManager: SessionManager) {

    private fun api() = RetrofitClient.authenticated(
        sessionManager.getAccessToken() ?: ""
    )

    suspend fun getJourney(): Perjalanan? {
        val uid = sessionManager.getUserId() ?: return null
        return try {
            val response = api().getJourneyByUser(
                idPengguna = "eq.$uid",
                select = "*",
                order = "id.desc",
                limit = 1
            )
            if (response.isSuccessful) {
                response.body()?.firstOrNull()?.toModel()
            } else {
                Log.e(TAG, "getJourney gagal: ${response.code()} ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getJourney exception: ${e.message}", e)
            null
        }
    }

    suspend fun getJourneyById(journeyId: String): Perjalanan? {
        return try {
            val response = api().getJourneyById("eq.$journeyId")
            if (response.isSuccessful) {
                response.body()?.firstOrNull()?.toModel()
            } else {
                Log.e(TAG, "getJourneyById gagal: ${response.code()} ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getJourneyById exception: ${e.message}", e)
            null
        }
    }

    suspend fun getAllJourneys(): List<Perjalanan> {
        val uid = sessionManager.getUserId() ?: return emptyList()
        return try {
            val response = api().getJourneyByUser(
                idPengguna = "eq.$uid",
                select = "*",
                order = "id.desc",
                limit = 100
            )
            if (response.isSuccessful) {
                response.body()?.map { it.toModel() } ?: emptyList()
            } else {
                Log.e(TAG, "getAllJourneys gagal: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAllJourneys exception: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun createJourney(
        namaPerjalanan: String,
        tanggalBerangkat: String,
        tanggalSelesai: String,
        tipePerjalanan: String
    ): String? {
        val uid = sessionManager.getUserId() ?: return null
        return try {
            val request = CreateJourneyRequest(
                idPengguna = uid,
                namaPerjalanan = namaPerjalanan,
                tanggalBerangkat = tanggalBerangkat,
                tanggalSelesai = tanggalSelesai,
                tipePerjalanan = tipePerjalanan
            )
            val response = api().createJourney(request)
            if (response.isSuccessful) {
                val createdId = response.body()?.firstOrNull()?.id
                Log.d(TAG, "Journey dibuat: $createdId")
                createdId
            } else {
                Log.e(TAG, "createJourney gagal: ${response.code()} ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "createJourney exception: ${e.message}", e)
            null
        }
    }

    suspend fun updateDaftarPerjalanan(
        journeyId: String,
        daftarPerjalanan: List<tempatPerjalanan>
    ): Boolean {
        return try {
            val dtoList = daftarPerjalanan.map { it.toDto() }
            val response = api().updateJourney(
                journeyId = "eq.$journeyId",
                update = UpdateDaftarPerjalananRequest(daftarPerjalanan = dtoList)
            )
            if (response.isSuccessful) {
                Log.d(TAG, "daftarPerjalanan diperbarui untuk journey: $journeyId")
                true
            } else {
                Log.e(TAG, "updateDaftarPerjalanan gagal: ${response.code()} ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateDaftarPerjalanan exception: ${e.message}", e)
            false
        }
    }

    suspend fun updateTipePerjalanan(journeyId: String, tipePerjalanan: String): Boolean {
        return try {
            val response = api().updateJourneyType(
                journeyId = "eq.$journeyId",
                update = UpdateTipePerjalananRequest(tipePerjalanan = tipePerjalanan)
            )
            if (response.isSuccessful) {
                Log.d(TAG, "tipePerjalanan diperbarui untuk journey: $journeyId")
                true
            } else {
                Log.e(TAG, "updateTipePerjalanan gagal: ${response.code()} ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateTipePerjalanan exception: ${e.message}", e)
            false
        }
    }

    suspend fun addTempatToJourney(
        journeyId: String,
        currentList: List<tempatPerjalanan>,
        newTempat: tempatPerjalanan
    ): Boolean {
        if (currentList.any { it.tempatId == newTempat.tempatId }) {
            Log.d(TAG, "Tempat sudah ada dalam daftar")
            return true
        }
        val updatedList = currentList.toMutableList().also { it.add(newTempat) }
        return updateDaftarPerjalanan(journeyId, updatedList)
    }

    suspend fun hapusTempatDariPerjalanan(
        journeyId: String,
        currentList: List<tempatPerjalanan>,
        tempatToRemove: tempatPerjalanan
    ): Boolean {
        val updatedList = currentList.filter { it.tempatId != tempatToRemove.tempatId }
        return updateDaftarPerjalanan(journeyId, updatedList)
    }

    suspend fun deleteJourney(journeyId: String): Boolean {
        return try {
            val response = api().deleteJourney("eq.$journeyId")
            if (response.isSuccessful) {
                Log.d(TAG, "Journey $journeyId berhasil dihapus")
                true
            } else {
                Log.e(TAG, "deleteJourney gagal: ${response.code()} ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteJourney exception: ${e.message}", e)
            false
        }
    }

    private fun JourneyDto.toModel(): Perjalanan {
        return Perjalanan(
            id = id,
            namaPerjalanan = namaPerjalanan,
            tanggalBerangkat = tanggalBerangkat,
            tanggalSelesai = tanggalSelesai,
            idPengguna = idPengguna,
            tipePerjalanan = tipePerjalanan,
            jamMulai = jamMulai,
            jamSelesai = jamSelesai,
            daftarPerjalanan = daftarPerjalanan.map { it.toModel() }
        )
    }
}
