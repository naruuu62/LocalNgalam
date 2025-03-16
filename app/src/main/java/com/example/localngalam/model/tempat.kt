import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Data class sesuai dengan struktur di Firestore
data class Tempat(
    var id: String = "",
    val address: String = "",
    val category: String = "",
    val close: String = "",
    val deskripsi: String = "",
    val open: String = "",
    val phoneNumber: String = "",
    val priceRange: Long = 0,
    val tags: List<Long> = emptyList(),
    val gambar: String = ""
)

@Composable
fun tempatScreen(navController: NavController, viewModel: planViewModel = viewModel()) {
    val tempatList by viewModel.tempatList.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.padding(20.dp),
                onClick = { selectedCategory = "All"; viewModel.getTempatFilter("All") },

                ) { Text("All") }
        }
        Button(
            modifier = Modifier.padding(20.dp),
            onClick = { selectedCategory = "Cafe"; viewModel.getTempatFilter("Cafe") },

            ) { Text("Cafe") }
        Button(
            modifier = Modifier.padding(20.dp),
            onClick = { selectedCategory = "Restoran"; viewModel.getTempatFilter("Restoran") },

            ) { Text("Restoran") }
        Button(
            modifier = Modifier.padding(20.dp),
            onClick = { selectedCategory = "Alam"; viewModel.getTempatFilter("Alam") },

            ) { Text("Alam") }

    }

    if (selectedCategory != null) {
        LazyColumn {
            items(tempatList) { tempat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()){
                            AsyncImage(
                                model = tempat.gambar,
                                contentDescription = null,
                                modifier = Modifier.size(150.dp).clip(RoundedCornerShape(30.dp)))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(tempat.id, fontWeight = FontWeight.Bold, fontFamily = poppinsFont)
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(text = "Alamat: ${tempat.address}")
                                Text(text = "Kategori: ${tempat.category}")
                                Text(text = "Waktu Tutup: ${tempat.close}")
                                Text(text = "Waktu Buka: ${tempat.open}")
                                Text(text = "Nomor Telepon: ${tempat.phoneNumber}")
                                Text(text = "Deskripsi: ${tempat.deskripsi}")
                            }

                        }




                    }

                }
            }
        }

    }
}







