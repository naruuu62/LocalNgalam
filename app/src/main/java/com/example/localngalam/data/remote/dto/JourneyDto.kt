package com.example.localngalam.data.remote.dto

import com.example.localngalam.model.tempatPerjalanan
import com.google.gson.annotations.SerializedName

/**
 * DTO for the `journey` table in Supabase.
 * [daftarPerjalanan] is stored as JSONB in Postgres.
 */
data class JourneyDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("id_pengguna") val idPengguna: String = "",
    @SerializedName("nama_perjalanan") val namaPerjalanan: String = "",
    @SerializedName("tanggal_berangkat") val tanggalBerangkat: String = "",
    @SerializedName("tanggal_selesai") val tanggalSelesai: String = "",
    @SerializedName("tipe_perjalanan") val tipePerjalanan: String = "",
    @SerializedName("jam_mulai") val jamMulai: String = "",
    @SerializedName("jam_selesai") val jamSelesai: String = "",
    @SerializedName("daftar_perjalanan") val daftarPerjalanan: List<TempatPerjalananDto> = emptyList()
)

data class TempatPerjalananDto(
    @SerializedName("tempatId") val tempatId: String = "",
    @SerializedName("namaTempat") val namaTempat: String = "",
    @SerializedName("jamMulai") val jamMulai: String = "",
    @SerializedName("jamSelesai") val jamSelesai: String = "",
    @SerializedName("gambar") val gambar: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("deskripsi") val deskripsi: String = "",
    @SerializedName("nomorTelepon") val nomorTelepon: String = "",
    @SerializedName("tanggal") val tanggal: String = ""
)

// ─── Request bodies ────────────────────────────────────────────────────────

data class CreateJourneyRequest(
    @SerializedName("id_pengguna") val idPengguna: String,
    @SerializedName("nama_perjalanan") val namaPerjalanan: String,
    @SerializedName("tanggal_berangkat") val tanggalBerangkat: String,
    @SerializedName("tanggal_selesai") val tanggalSelesai: String,
    @SerializedName("tipe_perjalanan") val tipePerjalanan: String,
    @SerializedName("daftar_perjalanan") val daftarPerjalanan: List<TempatPerjalananDto> = emptyList()
)

data class UpdateDaftarPerjalananRequest(
    @SerializedName("daftar_perjalanan") val daftarPerjalanan: List<TempatPerjalananDto>
)

// ─── Mapper extensions ─────────────────────────────────────────────────────

fun TempatPerjalananDto.toModel(): tempatPerjalanan = tempatPerjalanan(
    tempatId = tempatId,
    namaTempat = namaTempat,
    jamMulai = jamMulai,
    jamSelesai = jamSelesai,
    gambar = gambar,
    address = address,
    deskripsi = deskripsi,
    nomorTelepon = nomorTelepon,
    tanggal = tanggal
)

fun tempatPerjalanan.toDto(): TempatPerjalananDto = TempatPerjalananDto(
    tempatId = tempatId,
    namaTempat = namaTempat,
    jamMulai = jamMulai,
    jamSelesai = jamSelesai,
    gambar = gambar,
    address = address,
    deskripsi = deskripsi,
    nomorTelepon = nomorTelepon,
    tanggal = tanggal
)
