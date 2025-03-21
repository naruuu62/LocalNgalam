import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.snap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.model.UserData
import com.example.localngalam.model.tempatPerjalanan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//filter data sob by category

class planViewModel : ViewModel() {
    private val db = Firebase.firestore
    private var documentId: String? = null

    private val _riwayatPerjalanan = MutableStateFlow<List<Perjalanan>>(emptyList())
    val riwayatPerjalanan: StateFlow<List<Perjalanan>> get() = _riwayatPerjalanan

    private val _journeyData = MutableStateFlow<Perjalanan?>(null)
    val journeyData: StateFlow<Perjalanan?> get() = _journeyData

    private val _tanggalBerangkat = MutableStateFlow<String?>(null)
    val tanggalBerangkat: StateFlow<String?> = _tanggalBerangkat

    fun setTanggalBerangkat(tanggal: String?) {
        _tanggalBerangkat.value = tanggal
    }

    private val _namaPerjalanan = MutableStateFlow<String?>(null)
    val namaPerjalanan: StateFlow<String?> = _namaPerjalanan

    fun setNamaPerjalanan(nama: String?) {
        _namaPerjalanan.value = nama
    }

    var currentJourneyId: String? = null

    fun saveJourney(
        namaPerjalanan: String,
        tanggalBerangkat: String,
        tanggalSelesai: String,
        tipePerjalanan: String,
    ) {
        val uid = auth.currentUser?.uid ?: return

        val journey = hashMapOf(
            "idPengguna" to uid,
            "namaPerjalanan" to namaPerjalanan,
            "tanggalBerangkat" to tanggalBerangkat,
            "tanggalSelesai" to tanggalSelesai,
            "tipePerjalanan" to tipePerjalanan,
            "daftarPerjalanan" to emptyList<Map<String, Any>>()
        )
        db.collection("journey").document(uid).set(journey, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Firestore", "Perjalanan berhasil disimpan")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Gagal menyimpan perjalanan", e)
            }
    }

    fun getJourney() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = db.collection("journey").document(uid).get().await()
                if (snapshot.exists()) {
                    val journey = snapshot.toObject(Perjalanan::class.java)
                    _journeyData.value = journey

                }
            } catch (e: Exception) {
                Log.e("Firestore", "Gagal mengambil perjalanan", e)
            }

        }
    }

    fun setDocumentId(id: String) {
        documentId = id
    }

    fun saveToFirestoreJadwal(perjalanan: Perjalanan, onSuccess: () -> Unit) {

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val db = Firebase.firestore
        val uid = user?.uid
        if (uid != null) {
            val perjalananData = hashMapOf(
                "namaPerjalanan" to perjalanan.namaPerjalanan,  // Simpan nama perjalanan juga
                "tanggalBerangkat" to perjalanan.tanggalBerangkat,
                "tanggalSelesai" to perjalanan.tanggalSelesai,
                "idPengguna" to uid,
                "tipePerjalanan" to perjalanan.tipePerjalanan
                // "selectedDates" to getDatesBetween(startDate, endDate).map { it.toString() }
            )

            db.collection("journey").document(uid)
                .set(perjalananData, SetOptions.merge())
                .addOnSuccessListener {
                    onSuccess()
                }
        }
    }


    fun updatePlan(tipePerjalanan: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            db.collection("journey").document(uid)
                .update("tipePerjalanan", tipePerjalanan)
                .addOnSuccessListener {
                    Log.d("Firestore", "Tipe perjalanan berhasil diperbarui: $tipePerjalanan")
                }

        }

    }


    private val _tempatList = MutableStateFlow<List<Tempat>>(emptyList())
    val tempatList: StateFlow<List<Tempat>> get() = _tempatList

    init {
        getTempatFilter("All")
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

    fun getTempatFilter(category: String) {
        val query = if (category == "All") db.collection("tempat") else db.collection("tempat")
            .whereEqualTo("Category", category)
        query.get().addOnSuccessListener { snapshot ->
            _tempatList.value = snapshot.toObjects(Tempat::class.java)

            viewModelScope.launch {
                val tempatData = snapshot?.map { queryDocumentSnapshot ->
                    val rawTags =
                        queryDocumentSnapshot.get("Tag") as? List<*> ?: emptyList<String>()
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

    fun updateDaftarPerjalanan(
        tempatId: String,
        namaTempat: String,
        jamMulai: String,
        jamSelesai: String,
        address: String,
        nomorTelepon: String,
        deskripsi: String,
        gambar: String,



    ) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("journey").document(uid)

        viewModelScope.launch {
            try {
                val docSnapshot = docRef.get().await()

                val daftarPerjalanan =
                    docSnapshot.get("daftarPerjalanan") as? MutableList<Map<String, Any>>
                        ?: mutableListOf()

                val existingIndex = daftarPerjalanan.indexOfFirst { it["tempatId"] == tempatId }

                if (existingIndex != -1) {
                    daftarPerjalanan[existingIndex] = mapOf(
                        "tempatId" to tempatId,
                        "namaTempat" to namaTempat,
                        "jamMulai" to jamMulai,
                        "jamSelesai" to jamSelesai,
                        "gambar" to gambar,
                        "address" to address,
                        "deskripsi" to deskripsi,
                        "nomorTelepon" to nomorTelepon,

                    )
                } else {
                    daftarPerjalanan.add(
                        mapOf(
                            "tempatId" to tempatId,
                            "namaTempat" to namaTempat,
                            "jamMulai" to jamMulai,
                            "jamSelesai" to jamSelesai,
                            "gambar" to gambar,
                            "address" to address,
                            "deskripsi" to deskripsi,
                            "nomorTelepon" to nomorTelepon,

                        )
                    )
                }

                docRef.update("daftarPerjalanan", daftarPerjalanan).await()
                Log.d("Firestore", "Daftar perjalanan diperbarui")
            } catch (e: Exception) {
                Log.e("Firestore", "Gagal memperbarui daftar perjalanan", e)
            }
        }
    }


    fun hapusTempatDariFirestore(tempat: tempatPerjalanan) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val docRef = db.collection("journey").document(uid)

            val tempatData = hashMapOf(
                "tempatId" to tempat.tempatId,
                "namaTempat" to tempat.namaTempat,
                "jamMulai" to tempat.jamMulai,
                "jamSelesai" to tempat.jamSelesai
            )

            docRef.update("daftarPerjalanan", FieldValue.arrayRemove(tempatData))
                .addOnSuccessListener {
                    Log.d("Firestore", "Tempat berhasil dihapus: $tempatData")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Gagal menghapus tempat: $e")
                }
        }
    }


    private val _userJourney = MutableStateFlow<Perjalanan?>(null)
    val userJourney: StateFlow<Perjalanan?> = _userJourney
    val auth = FirebaseAuth.getInstance()

    init {
        val idPengguna = FirebaseAuth.getInstance().currentUser?.uid
        if (idPengguna != null) {
            getUserJourney()
        } else {
            println("User belum login")
        }
    }

    private fun getUserJourney() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            db.collection("journey").document(uid) // Pakai UID user sebagai documentId
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        try {
                            // Periksa apakah datanya berbentuk Map sebelum parsing
                            val data = snapshot.data
                            if (data is Map<*, *>) {
                                val perjalanan = snapshot.toObject(Perjalanan::class.java)
                                _userJourney.value = perjalanan
                                Log.d("FirestoreData", "Data perjalanan: $perjalanan")

                                // Lakukan sesuatu dengan data perjalanan
                            } else {
                                Log.e("FirestoreError", "Data bukan Map: ${data.toString()}")
                            }
                        } catch (e: Exception) {
                            Log.e("FirestoreError", "Error parsing data perjalanan", e)
                        }
                    } else {
                        Log.e("FirestoreError", "Dokumen tidak ditemukan")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Gagal mengambil perjalanan: ${e.message}")
                }
        } else {
            Log.e("Firestore", "Gagal mendapatkan UID pengguna")
        }
    }


    fun updateJourneyTime(
        uid: String,
        tempatId: String,
        jamMulai: String,
        jamSelesai: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("perjalanan").document(uid)

        viewModelScope.launch {
            try {
                val docSnapshot = docRef.get().await()

                if (!docSnapshot.exists()) {
                    Log.e("Firestore", "Dokumen perjalanan dengan UID $uid tidak ditemukan!")
                    onFailure()
                    return@launch
                }

                val daftarPerjalanan =
                    docSnapshot.get("daftarPerjalanan") as? MutableList<Map<String, Any>>
                        ?: mutableListOf()

                // Cari tempat yang cocok dan update jamMulai & jamSelesai
                val updatedList = daftarPerjalanan.map { tempat ->
                    if (tempat["id"] == tempatId) { // Sesuaikan key ID dengan Firestore
                        tempat + mapOf("jamMulai" to jamMulai, "jamSelesai" to jamSelesai)
                    } else tempat
                }

                docRef.update("daftarPerjalanan", updatedList).await()
                Log.d("Firestore", "Jam perjalanan berhasil diperbarui untuk tempatId: $tempatId")
                onSuccess()

            } catch (e: Exception) {
                Log.e("Firestore", "Gagal memperbarui jam perjalanan", e)
                onFailure()
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
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("journey").document(uid)

        viewModelScope.launch {
            try {
                val docSnapshot = docRef.get().await()

                if (!docSnapshot.exists()) {
                    Log.e("Firestore", "Dokumen perjalanan tidak ditemukan, membuat baru...")
                    val newJourney = mapOf(
                        "daftarPerjalanan" to listOf(
                            mapOf(
                                "id" to tempatId,
                                "namaTempat" to namaTempat,
                                "jamMulai" to jamMulai,
                                "jamSelesai" to jamSelesai,
                                "gambar" to gambar,
                                "address" to address,
                                "deskripsi" to deskripsi,
                                "nomorTelepon" to nomorTelepon,

                            )
                        )
                    )
                    docRef.set(newJourney).await()
                    onSuccess()
                    return@launch
                }

                // Jika dokumen sudah ada, ambil daftar perjalanan lama
                val daftarPerjalanan =
                    docSnapshot.get("daftarPerjalanan") as? MutableList<Map<String, Any>>
                        ?: mutableListOf()

                if (daftarPerjalanan.any { it["id"] == tempatId }) {
                    Log.d("Firestore", "Tempat sudah ada, tidak ditambahkan lagi")
                    onSuccess()
                    return@launch
                }
                // Tambahkan tempat baru
                val updatedList = daftarPerjalanan.toMutableList().apply {
                    add(
                        mapOf(
                            "id" to tempatId,
                            "namaTempat" to namaTempat,
                            "jamMulai" to jamMulai,
                            "jamSelesai" to jamSelesai,
                            "gambar" to gambar,
                            "address" to address,
                            "deskripsi" to deskripsi,
                            "nomorTelepon" to nomorTelepon,

                        )
                    )
                }

                // Simpan ke Firestore
                docRef.update("daftarPerjalanan", updatedList).await()
                onSuccess()
            } catch (e: Exception) {
                Log.e("Firestore", "Gagal menambahkan tempat ke perjalanan", e)
                onFailure(e)
            }
        }
    }

}