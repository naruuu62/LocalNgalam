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

@Composable
fun testing(navController: NavController) {
    val db = Firebase.firestore

    // State untuk menyimpan daftar tempat dan status loading
    var tempatList by remember { mutableStateOf(listOf<Tempat>()) }
    var isLoading by remember { mutableStateOf(true) }

    // State untuk pilihan sorting
    var selectedSort by remember { mutableStateOf("Price Ascending") }

    // Function untuk mengambil data berdasarkan pilihan sorting
    fun fetchTempatData(sortType: String) {
        isLoading = true

        val query = when (sortType) {
            "Price Ascending" -> db.collection("tempat").orderBy("Price Range", com.google.firebase.firestore.Query.Direction.ASCENDING)
            "Price Descending" -> db.collection("tempat").orderBy("Price Range", com.google.firebase.firestore.Query.Direction.DESCENDING)
            "Rating Ascending" -> db.collection("tempat").orderBy("Rating", com.google.firebase.firestore.Query.Direction.ASCENDING)
            "Rating Descending" -> db.collection("tempat").orderBy("Rating", com.google.firebase.firestore.Query.Direction.DESCENDING)
            else -> db.collection("tempat")
        }

        query.addSnapshotListener { snapshot, e ->
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

    // Ambil data pertama kali sesuai sorting default (Price Ascending)
    LaunchedEffect(Unit) {
        fetchTempatData(selectedSort)
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

        // Dropdown untuk memilih sorting
        var expanded by remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded = true }) {
                Text("Urutkan: $selectedSort")
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Termurah → Termahal") }, onClick = {
                    selectedSort = "Price Ascending"
                    fetchTempatData("Price Ascending")
                    expanded = false
                })
                DropdownMenuItem(text = { Text("Termahal → Termurah") }, onClick = {
                    selectedSort = "Price Descending"
                    fetchTempatData("Price Descending")
                    expanded = false
                })
                DropdownMenuItem(text = { Text("Rating Tertinggi → Terendah") }, onClick = {
                    selectedSort = "Rating Descending"
                    fetchTempatData("Rating Descending")
                    expanded = false
                })
                DropdownMenuItem(text = { Text("Rating Terendah → Tertinggi") }, onClick = {
                    selectedSort = "Rating Ascending"
                    fetchTempatData("Rating Ascending")
                    expanded = false
                })
            }
        }

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
