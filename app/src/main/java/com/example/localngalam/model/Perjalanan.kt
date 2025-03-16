package com.example.localngalam.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class  Perjalanan(
    val namaPerjalanan: String = "",
    val tanggalBerangkat: String = "",
    val tanggalSelesai: String= "",
    val tujuan: String = "",
    val kategoriTujuan: String = "",
    val idPengguna: String = "",
    val tipePerjalanan: String = ""
)

fun saveToFirestoreJadwal(perjalanan: Perjalanan) {
    if (perjalanan.tanggalBerangkat.isBlank() || perjalanan.tanggalSelesai.isBlank()) {
        Log.e("Firestore", "Tanggal tidak boleh kosong!")
        return
    }

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val db = Firebase.firestore
    val perjalananData = hashMapOf(
        "namaPerjalanan" to perjalanan.namaPerjalanan,  // Simpan nama perjalanan juga
        "tanggalBerangkat" to perjalanan.tanggalBerangkat,
        "tanggalSelesai" to perjalanan.tanggalSelesai,
        "idPengguna" to user?.uid,
        "tipePerjalanan" to perjalanan.tipePerjalanan
        // "selectedDates" to getDatesBetween(startDate, endDate).map { it.toString() }
    )

    db.collection("journeys").add(perjalananData)
}