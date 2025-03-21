import Tempat
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class homeViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _tempatPerKategori = MutableStateFlow<Map<String, List<Tempat>>>(emptyMap())
    val tempatPerKategori: StateFlow<Map<String, List<Tempat>>> get() = _tempatPerKategori

    private val kategoriList = listOf("Restoran", "Alam", "Cafe")

    fun getTempatById(tempatId: String): Flow<Tempat?> = flow {
        val snapshot = db.collection("tempat").document(tempatId).get().await()
        emit(snapshot.toObject(Tempat::class.java))
    }

    init {
        getTempatPerKategori()
    }

    private fun getTempatPerKategori() {
        viewModelScope.launch {
            val kategoriData = mutableMapOf<String, List<Tempat>>()

            for (kategori in kategoriList) {
                try {
                    val snapshot = db.collection("tempat")
                        .whereEqualTo("Category", kategori)
                        .get()
                        .await() // Menunggu hasil Firestore Query

                    val tempatData = snapshot.toObjects(Tempat::class.java).map { tempat ->
                        Tempat(
                            id = tempat.id,
                            address = tempat.address,
                            category = tempat.category,
                            close = tempat.close,
                            deskripsi = tempat.deskripsi,
                            open = tempat.open,
                            phoneNumber = tempat.phoneNumber,
                            priceRange = tempat.priceRange,
                            tags = tempat.tags?.mapNotNull { (it as? Number)?.toLong() }
                                ?.map { getTagNilai(it) } ?: emptyList(),
                            gambar = tempat.gambar
                        )
                    }

                    kategoriData[kategori] = tempatData
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Gagal mengambil data kategori $kategori: ${e.message}")
                }
            }

            // Update state setelah semua kategori selesai diproses
            _tempatPerKategori.value = kategoriData
        }
    }

    fun getTagNilai(nilai: Long): String {
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
