import Tempat
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.repository.JourneyRepository
import com.example.localngalam.data.repository.TempatRepository
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.model.tempatPerjalanan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class planViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val tempatRepository = TempatRepository(sessionManager)
    private val journeyRepository = JourneyRepository(sessionManager)

    // ─── Journey ID state (UUID from Supabase after creation) ────────────────
    private var currentJourneyId: String? = null

    // ─── Tempat list for plan selection ───────────────────────────────────────
    private val _tempatList = MutableStateFlow<List<Tempat>>(emptyList())
    val tempatList: StateFlow<List<Tempat>> get() = _tempatList

    // ─── Current user's active journey ────────────────────────────────────────
    private val _userJourney = MutableStateFlow<Perjalanan?>(null)
    val userJourney: StateFlow<Perjalanan?> = _userJourney

    // ─── Journey detail ───────────────────────────────────────────────────────
    private val _journeyData = MutableStateFlow<Perjalanan?>(null)
    val journeyData: StateFlow<Perjalanan?> get() = _journeyData

    // ─── Riwayat perjalanan ───────────────────────────────────────────────────
    private val _riwayatPerjalanan = MutableStateFlow<List<Perjalanan>>(emptyList())
    val riwayatPerjalanan: StateFlow<List<Perjalanan>> get() = _riwayatPerjalanan

    // ─── UI helpers ───────────────────────────────────────────────────────────
    private val _tanggalBerangkat = MutableStateFlow<String?>(null)
    val tanggalBerangkat: StateFlow<String?> = _tanggalBerangkat

    private val _namaPerjalanan = MutableStateFlow<String?>(null)
    val namaPerjalanan: StateFlow<String?> = _namaPerjalanan

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun setTanggalBerangkat(tanggal: String?) { _tanggalBerangkat.value = tanggal }
    fun setNamaPerjalanan(nama: String?) { _namaPerjalanan.value = nama }

    init {
        getTempatFilter("All")
        getUserJourney()
    }

    // ─── Tempat ──────────────────────────────────────────────────────────────

    fun getTempatFilter(category: String) {
        viewModelScope.launch {
            try {
                val result = tempatRepository.getTempat(category = category)
                _tempatList.value = result
            } catch (e: Exception) {
                Log.e("planViewModel", "Gagal mengambil tempat: ${e.message}", e)
            }
        }
    }

    // ─── Journey CRUD ─────────────────────────────────────────────────────────

    fun saveJourney(
        namaPerjalanan: String,
        tanggalBerangkat: String,
        tanggalSelesai: String,
        tipePerjalanan: String,
        onSuccess: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val journeyId = journeyRepository.createJourney(
                namaPerjalanan = namaPerjalanan,
                tanggalBerangkat = tanggalBerangkat,
                tanggalSelesai = tanggalSelesai,
                tipePerjalanan = tipePerjalanan
            )
            if (journeyId != null) {
                currentJourneyId = journeyId
                Log.d("planViewModel", "Journey berhasil disimpan: $journeyId")
                onSuccess(journeyId)
            } else {
                Log.e("planViewModel", "Gagal menyimpan journey")
            }
            _isLoading.value = false
        }
    }

    fun getJourney() {
        viewModelScope.launch {
            val journey = journeyRepository.getJourney()
            _journeyData.value = journey
        }
    }

    private fun getUserJourney() {
        viewModelScope.launch {
            val journey = journeyRepository.getJourney()
            _userJourney.value = journey
            Log.d("planViewModel", "User journey: $journey")
        }
    }

    fun updatePlan(tipePerjalanan: String) {
        val journeyId = currentJourneyId ?: run {
            Log.e("planViewModel", "Journey ID belum ada, tidak bisa update")
            return
        }
        viewModelScope.launch {
            // Re-fetch journey to get current daftarPerjalanan, then update tipe only
            // For now we patch by creating a new journey update — or simply call getJourney + updateDaftar
            Log.d("planViewModel", "Update tipe perjalanan: $tipePerjalanan")
        }
    }

    // ─── Daftar Perjalanan ────────────────────────────────────────────────────

    fun updateDaftarPerjalanan(
        tempatId: String,
        namaTempat: String,
        jamMulai: String,
        jamSelesai: String,
        address: String,
        nomorTelepon: String,
        deskripsi: String,
        gambar: String
    ) {
        val journeyId = currentJourneyId
        if (journeyId == null) {
            Log.e("planViewModel", "Journey ID null, tidak bisa update daftar perjalanan")
            return
        }

        viewModelScope.launch {
            val currentJourney = journeyRepository.getJourneyById(journeyId)
            val currentList = currentJourney?.daftarPerjalanan?.toMutableList() ?: mutableListOf()

            val newTempat = tempatPerjalanan(
                tempatId = tempatId,
                namaTempat = namaTempat,
                jamMulai = jamMulai,
                jamSelesai = jamSelesai,
                address = address,
                nomorTelepon = nomorTelepon,
                deskripsi = deskripsi,
                gambar = gambar
            )

            val existingIndex = currentList.indexOfFirst { it.tempatId == tempatId }
            if (existingIndex != -1) {
                currentList[existingIndex] = newTempat
            } else {
                currentList.add(newTempat)
            }

            val success = journeyRepository.updateDaftarPerjalanan(journeyId, currentList)
            if (success) {
                _userJourney.value = currentJourney?.copy(daftarPerjalanan = currentList)
            }
        }
    }

    fun addTempatToJourney(
        uid: String,
        tempatId: String,
        namaTempat: String,
        jamMulai: String,
        jamSelesai: String,
        gambar: String,
        address: String,
        deskripsi: String,
        nomorTelepon: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val journeyId = currentJourneyId
        if (journeyId == null) {
            onFailure(Exception("Journey ID null"))
            return
        }

        viewModelScope.launch {
            try {
                val currentJourney = journeyRepository.getJourneyById(journeyId)
                val currentList = currentJourney?.daftarPerjalanan ?: emptyList()

                val newTempat = tempatPerjalanan(
                    tempatId = tempatId,
                    namaTempat = namaTempat,
                    jamMulai = jamMulai,
                    jamSelesai = jamSelesai,
                    gambar = gambar,
                    address = address,
                    deskripsi = deskripsi,
                    nomorTelepon = nomorTelepon
                )

                val success = journeyRepository.addTempatToJourney(journeyId, currentList, newTempat)
                if (success) {
                    getUserJourney()
                    onSuccess()
                } else {
                    onFailure(Exception("Gagal menambah tempat"))
                }
            } catch (e: Exception) {
                Log.e("planViewModel", "addTempatToJourney exception: ${e.message}", e)
                onFailure(e)
            }
        }
    }

    fun hapusTempatDariFirestore(tempat: tempatPerjalanan) {
        val journeyId = currentJourneyId
        if (journeyId == null) {
            Log.e("planViewModel", "Journey ID null, tidak bisa hapus tempat")
            return
        }

        viewModelScope.launch {
            val currentJourney = journeyRepository.getJourneyById(journeyId)
            val currentList = currentJourney?.daftarPerjalanan ?: emptyList()
            val success = journeyRepository.hapusTempatDariPerjalanan(journeyId, currentList, tempat)
            if (success) {
                Log.d("planViewModel", "Tempat berhasil dihapus: ${tempat.namaTempat}")
                getUserJourney()
            }
        }
    }

    fun setDocumentId(id: String) {
        currentJourneyId = id
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

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