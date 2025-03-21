package com.example.localngalam.presentation.createPlan

import Tempat
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.localngalam.R
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.model.tempatPerjalanan
import com.example.localngalam.presentation.ui.theme.Blue
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.Blue4
import com.example.localngalam.presentation.ui.theme.Green2
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.ButtonNextCreatePlan
import com.example.localngalam.presentation.ui_component.ButtonPrevCreatePlan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath.documentId
import okhttp3.Request
import planViewModel
import java.nio.file.WatchEvent


@Composable
fun CreatePlanScreen3(navController: NavController, viewModel: planViewModel = viewModel()) {
    val documentId = navController.previousBackStackEntry?.savedStateHandle?.get<String>("documentId") ?: ""
    val tempatList by viewModel.tempatList.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedPlaces by remember { mutableStateOf<List<Tempat>>(emptyList()) }
    var selectedPlace by remember { mutableStateOf<Tempat?>(null) }
    var isAdding by remember {   mutableStateOf(false) }
    val tagListState = remember { mutableStateOf<List<Long>>(emptyList()) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""
    val tempatPerjalanan by viewModel.tempatList.collectAsState(initial = emptyList())





    fun hapusTempat(selectedPlace: Tempat, selectedPlaces: List<Tempat>, setSelectedPlaces: (List<Tempat>) -> Unit) {
        setSelectedPlaces(selectedPlaces.filter { it != selectedPlace })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Malang Trip",
                fontSize = 16.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Bold,
                color = Blue3,
                modifier = Modifier.padding(start = 16.dp)
            )

            Text(
                text = "3 of 4",
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Pilih rencana perjalanan", fontFamily = poppinsFont, fontSize = 12.sp, color = Blue2, modifier = Modifier.padding(start = 16.dp))

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = "Add",
                tint = Color.Unspecified,
                modifier = Modifier.clickable {
                    isAdding = !isAdding
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                if (!isAdding) {
                    Text(
                        text = "Bangun rencana perjalanan anda dengan memilih tiap kategori yang sesuai.",
                        fontSize = 12.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                } else {
                    selectedPlaces.forEachIndexed { index, selectedPlace ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if(index > 0){
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(40.dp)
                                        .background(Color.Gray)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .shadow(elevation = 12.dp, shape = RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = selectedPlace.gambar),
                                        contentDescription = "Tempat Image",
                                        modifier = Modifier
                                            .height(100.dp)
                                            .width(94.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = selectedPlace.id,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Blue3,
                                            fontFamily = poppinsFont
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = selectedPlace.address,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            fontFamily = poppinsFont
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row{
                                            Icon(painter = painterResource(R.drawable.jam), contentDescription = "telepon")
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(text = selectedPlace.open + " - " + selectedPlace.close, fontSize = 12.sp, color = Color.Black)

                                        }
                                        Spacer(modifier = Modifier.height(9.dp))
                                        Row() {
                                            Icon(painter = painterResource(R.drawable.telepon), contentDescription = "location")
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = selectedPlace.phoneNumber,
                                                fontSize = 12.sp,
                                                color = Color.Black,
                                                fontFamily = poppinsFont
                                            )
                                        }
                                    }
                                    Box() {
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Icon(
                                            painter = painterResource(id = R.drawable.trash),
                                            contentDescription = "Options",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(24.dp).clickable{
                                                hapusTempat(
                                                    selectedPlace,
                                                    selectedPlaces,
                                                    { updatedPlaces -> selectedPlaces = updatedPlaces })
                                            }
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {

                                }
                                Text(
                                    text = selectedPlace.deskripsi,
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    fontFamily = poppinsFont
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
        // Tombol Simpan Rencana Perjalanan dipindahkan ke sini
        Row {
            Spacer(modifier = Modifier.width(300.dp))
            Button(
                onClick = {
                    navController.navigate("history")
                },
                modifier = Modifier
                    .height(35.dp)
                    .width(120.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green2,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Simpan")
            }
        }

        if (showConfirmationDialog) {
            confirmationDialog(
                onDismissRequest = { showConfirmationDialog = false },
                onConfirmation = { jamMulai, jamSelesai ->
                    val index = selectedPlaces.indexOf(selectedPlace)
                    if (index != -1) {
                        selectedPlaces = selectedPlaces.toMutableList().apply {
                            this[index] = this[index].copy(
                                open = jamMulai,
                                close = jamSelesai
                            )
                        }
                    }

                    val tempatPerjalanan = selectedPlaces.map {
                            tempat -> tempatPerjalanan(
                        tempatId = tempat.id,
                        namaTempat = tempat.id,
                        jamMulai = jamMulai,
                        jamSelesai = jamSelesai
                    )


                    }
                    tempatPerjalanan.forEach {
                            tempat -> viewModel.updateDaftarPerjalanan(tempatId = tempat.tempatId, namaTempat = tempat.tempatId, jamMulai = jamMulai, jamSelesai = jamSelesai,
                        gambar = selectedPlace?.gambar ?: "",
                        address = selectedPlace?.address ?: "", deskripsi = selectedPlace?.deskripsi ?: "", nomorTelepon = selectedPlace?.phoneNumber ?: "")
                    }
                    showConfirmationDialog = false
                },
                selectedPlace = selectedPlaces.firstOrNull()
            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Pilih Kategori",
            fontSize = 14.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.SemiBold,
            color = Blue3,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            val categories = listOf("Semua", "Alam", "Cafe", "Restoran", "Wisata", "Wahana")
            items(categories) { category ->
                Button(
                    onClick = {
                        selectedCategory = category
                        viewModel.getTempatFilter(if (category == "Semua") "All" else category)
                    },
                    modifier = Modifier.padding(end = 8.dp).height(48.dp).width(100.dp).shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory == category) Blue else Color.White,
                        contentColor = if (selectedCategory == category) Color.White else Color.Black
                    )
                ) {
                    Text(text = category, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        tempatList.forEach { tempat ->
            Card(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(0.5.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp
                    )),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(model = tempat.gambar),
                            contentDescription = "Tempat Image",
                            modifier = Modifier
                                .width(90.dp)
                                .height(136.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tempat.id,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Blue3
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = tempat.address,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = Blue3,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append("Harga ")
                                    }
                                    withStyle(style = SpanStyle(color = Color.Black)) {
                                        append(": ${viewModel.getHarga(tempat.priceRange)} /pax")
                                    }
                                },

                                fontSize = 12.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = tempat.deskripsi,
                                fontSize = 12.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(7.dp))

                            LazyRow {
                                items(tempat.tags) { tag ->
                                    Button(
                                        onClick = { /* Handle tag click */ },
                                        shape = RoundedCornerShape(25.dp),
                                        modifier = Modifier.padding(end = 5.dp).height(32.dp).width(170.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Blue4,
                                            contentColor = Color.Black
                                        ),
                                        border = BorderStroke(1.dp, Blue3)
                                    ) {
                                        Text(text = tag, fontSize = 10.sp)
                                    }
                                }
                            }
                        }

                        Column {
                            Spacer(modifier = Modifier.height(23.dp))


                            Spacer(modifier = Modifier.width(5.dp))

                            Icon(
                                painter = painterResource(R.drawable.plus),
                                contentDescription = "tambahin",
                                modifier = Modifier.clickable {
                                    selectedPlace = tempat
                                    showConfirmationDialog = true
                                    selectedPlaces = selectedPlaces + tempat
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun confirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String) -> Unit,
    tempatId: String = "",
    namaTempat: String = "",
    selectedPlace: Tempat?,
    viewModel: planViewModel = viewModel()
) {
    val perjalanan by viewModel.userJourney.collectAsStateWithLifecycle()
    var startHour1 by remember { mutableStateOf("") }
    var startHour2 by remember { mutableStateOf("") }
    var startMinute1 by remember { mutableStateOf("") }
    var startMinute2 by remember { mutableStateOf("") }
    var endHour1 by remember { mutableStateOf("") }
    var endHour2 by remember { mutableStateOf("") }
    var endMinute1 by remember { mutableStateOf("") }
    var endMinute2 by remember { mutableStateOf("") }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ingin Menambahkan ke Itinerary?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A5BA0),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = perjalanan?.tanggalBerangkat ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Mulai", fontSize = 14.sp, color = Color(0xFF3A5BA0))
                        Spacer(modifier = Modifier.height(8.dp))
                        TimeInputRow(startHour1, startHour2, startMinute1, startMinute2) { index, value ->
                            when (index) {
                                0 -> startHour1 = value
                                1 -> startHour2 = value
                                2 -> startMinute1 = value
                                3 -> startMinute2 = value
                            }
                        }
                    }

                    Text(text = "-")

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Selesai", fontSize = 14.sp, color = Color(0xFF3A5BA0))
                        Spacer(modifier = Modifier.height(8.dp))
                        TimeInputRow(endHour1, endHour2, endMinute1, endMinute2) { index, value ->
                            when (index) {
                                0 -> endHour1 = value
                                1 -> endHour2 = value
                                2 -> endMinute1 = value
                                3 -> endMinute2 = value
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ButtonPrevCreatePlan(onClick = { onDismissRequest() })
                    ButtonNextCreatePlan(onClick = {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        if (uid.isNullOrEmpty()) {
                            Log.e("Firestore", "Gagal mendapatkan UID pengguna!")
                            return@ButtonNextCreatePlan
                        }

                        val jamMulai = "${startHour1}${startHour2}:${startMinute1}${startMinute2}"
                        val jamSelesai = "${endHour1}${endHour2}:${endMinute1}${endMinute2}"


                        onConfirmation(jamMulai, jamSelesai)
                    })
                }
            }
        }
    }
}

@Composable
fun TimeInputRow(
    hour1: String,
    hour2: String,
    minute1: String,
    minute2: String,
    onValueChange: (Int, String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TimeDigitInput(digit = hour1, onDigitChange = { onValueChange(0, it) })
        TimeDigitInput(digit = hour2, onDigitChange = { onValueChange(1, it) })
        Text(text = ":", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 4.dp))
        TimeDigitInput(digit = minute1, onDigitChange = { onValueChange(2, it) })
        TimeDigitInput(digit = minute2, onDigitChange = { onValueChange(3, it) })
    }
}

@Composable
fun TimeDigitInput(digit: String, onDigitChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF0F0F0)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = digit,
            onValueChange = { newValue ->
                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                    onDigitChange(newValue)
                }
            },
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(22.dp),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.Center) { innerTextField() }
            }
        )
    }
}


