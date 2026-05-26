package com.example.localngalam.model

/**
 * Domain model for a trip/journey.
 * Firebase imports removed — persistence handled by JourneyRepository via Supabase.
 */
data class Perjalanan(
    val namaPerjalanan: String = "",
    val tanggalBerangkat: String = "",
    val tanggalSelesai: String = "",
    val idPengguna: String = "",
    val tipePerjalanan: String = "",
    val daftarPerjalanan: List<tempatPerjalanan> = emptyList(),
    val jamMulai: String = "",
    val jamSelesai: String = ""
)
