import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Data class sesuai dengan struktur di Firestore
data class Tempat(
    val id: String = "",
    val address: String = "",
    val category: String = "",
    val close: String = "",
    val deskripsi: String = "",
    val open: String = "",
    val phoneNumber: String = "",
    val priceRange: Long = 0,
    val tags: List<Long> = emptyList()
)

// Composable untuk menampilkan data
@Composable
fun FirestoreDataScreen(navController: NavController) {
    // Inisialisasi Firestore
    val db = Firebase.firestore

    // State untuk menyimpan list data
    var tempatList by remember { mutableStateOf(listOf<Tempat>()) }
    var isLoading by remember { mutableStateOf(true) }

    // Mengambil data dari Firestore dengan snapshot listener untuk real-time updates
    LaunchedEffect(Unit) {
        db.collection("tempat")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    isLoading = false
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.map { document ->
                        Tempat(
                            id = document.id,
                            address = document.getString("Addres") ?: "",
                            category = document.getString("Category") ?: "",
                            close = document.getString("Close") ?: "",
                            deskripsi = document.getString("Deskripsi") ?: "",
                            open = document.getString("Open") ?: "",
                            phoneNumber = document.getString("Phone Number") ?: "",
                            priceRange = document.getLong("Price Range") ?: 0,
                            tags = document.get("Tag") as? List<Long> ?: emptyList()
                        )
                    }
                    tempatList = list
                    isLoading = false
                }
            }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Daftar Tempat",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(tempatList) { tempat ->
                    TempatCard(tempat = tempat)
                }
            }
        }
    }
}

@Composable
fun TempatCard(tempat: Tempat) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = tempat.id, // Nama tempat (misalnya: AYAM BAKAR WONG SOLO)
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Alamat: ${tempat.address}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Kategori: ${tempat.category}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Deskripsi: ${tempat.deskripsi}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Jam Buka: ${tempat.open} - ${tempat.close}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Nomor Telepon: ${tempat.phoneNumber}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Harga: ${tempat.priceRange}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tag: ${tempat.tags.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}