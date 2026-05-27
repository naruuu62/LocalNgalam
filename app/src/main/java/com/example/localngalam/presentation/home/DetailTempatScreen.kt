    package com.example.localngalam.presentation.home

    import Tempat
    import androidx.compose.foundation.Image
    import androidx.compose.runtime.Composable
    import com.example.localngalam.presentation.ui_component.Navbar

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.automirrored.filled.ArrowBack
    import androidx.compose.material.icons.filled.Delete
    import androidx.compose.material.icons.filled.Edit
    import androidx.compose.material.icons.filled.Favorite
    import androidx.compose.material.icons.filled.FavoriteBorder
    import androidx.compose.material.icons.filled.Star
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.compose.ui.window.Dialog
    import androidx.lifecycle.compose.collectAsStateWithLifecycle
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import coil.compose.rememberAsyncImagePainter
    import com.example.localngalam.R
    import com.example.localngalam.data.local.SessionManager
    import com.example.localngalam.data.repository.BucketListRepository
    import com.example.localngalam.data.repository.Review
    import com.example.localngalam.presentation.ui.theme.Blue3
    import com.example.localngalam.presentation.ui.theme.Blue4
    import com.example.localngalam.presentation.ui.theme.Green
    import com.example.localngalam.presentation.ui.theme.poppinsFont
    import kotlinx.coroutines.launch

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DetailTempatScreen(
        navController: NavController,
        sharedViewModel: sharedViewModel = viewModel(),
        reviewViewModel: ReviewViewModel = viewModel(),
        tempat: Tempat
    ) {
        val reviews by reviewViewModel.reviews.collectAsStateWithLifecycle()
        val isLoadingReview by reviewViewModel.isLoading.collectAsStateWithLifecycle()
        var showAddReviewDialog by remember { mutableStateOf(false) }
        var showEditReviewDialog by remember { mutableStateOf(false) }
        var editingReview by remember { mutableStateOf<Review?>(null) }
        var isBucketListed by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        // Bucket List Repository
        val context = LocalContext.current
        val sessionManager = remember { SessionManager(context) }
        val bucketListRepository = remember { BucketListRepository(sessionManager) }

        // Load reviews & cek bucket list saat buka halaman
        LaunchedEffect(tempat.id) {
            reviewViewModel.fetchReviews(tempat.id)
            // Cek apakah tempat ini sudah ada di bucket list
            isBucketListed = bucketListRepository.isInBucketList(tempat.id)
        }

        val avgRating by remember {
            derivedStateOf { reviewViewModel.averageRating() }
        }

        // Dialog Tambah Ulasan
        if (showAddReviewDialog) {
            AddReviewDialog(
                onDismiss = { showAddReviewDialog = false },
                onSubmit = { rating, comment ->
                    reviewViewModel.addReview(tempat.id, rating, comment) {
                        showAddReviewDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Ulasan berhasil ditambahkan!")
                        }
                    }
                }
            )
        }

        // Dialog Edit Ulasan
        if (showEditReviewDialog && editingReview != null) {
            AddReviewDialog(
                onDismiss = {
                    showEditReviewDialog = false
                    editingReview = null
                },
                onSubmit = { rating, comment ->
                    reviewViewModel.updateReview(
                        reviewId = editingReview!!.id,
                        destinationId = tempat.id,
                        rating = rating,
                        comment = comment
                    ) {
                        showEditReviewDialog = false
                        editingReview = null
                        scope.launch {
                            snackbarHostState.showSnackbar("Ulasan berhasil diperbarui!")
                        }
                    }
                },
                initialRating = editingReview!!.rating,
                initialComment = editingReview!!.comment,
                dialogTitle = "Edit Ulasan"
            )
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = tempat.namaLokasi.ifBlank { "Detail Tempat" },
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFont,
                            color = Blue3
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color.White),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if (!isBucketListed) {
                                    val success = bucketListRepository.addToBucketList(tempat.id)
                                    if (success) {
                                        isBucketListed = true
                                        snackbarHostState.showSnackbar("Tersimpan ke Bucket List!")
                                    } else {
                                        snackbarHostState.showSnackbar("Gagal menyimpan ke Bucket List")
                                    }
                                } else {
                                    val success = bucketListRepository.deleteFromBucketList(tempat.id)
                                    if (success) {
                                        isBucketListed = false
                                        snackbarHostState.showSnackbar("Dihapus dari Bucket List")
                                    } else {
                                        snackbarHostState.showSnackbar("Gagal menghapus dari Bucket List")
                                    }
                                }
                            }
                        }) {
                            Icon(
                                imageVector = if (isBucketListed) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Bucket List",
                                tint = if (isBucketListed) Color.Red else Color.Gray
                            )
                        }
                    }
                )
            },
            bottomBar = {
                Navbar(
                    onHomeClick    = { navController.navigate("home") },
                    onSearchClick  = { navController.navigate("search") },
                    onPlusClick    = { navController.navigate("add_plan") },
                    onHistoryClick = { navController.navigate("history") },
                    onProfileClick = { navController.navigate("profile") }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                item {
                    // Alamat
                    Text(
                        text = tempat.address.ifBlank { "Alamat tidak tersedia" },
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Gambar utama
                    Image(
                        painter = rememberAsyncImagePainter(model = tempat.gambar),
                        contentDescription = "Gambar ${tempat.namaLokasi}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.padding(16.dp)) {

                        // Rating summary
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val ratingNow = if (reviews.isEmpty()) 0f else reviews.map { it.rating }.average().toFloat()
                            Text(text = "${reviews.size} ulasan", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (ratingNow > 0) "%.1f".format(ratingNow) else "-",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tags
                        if (tempat.tags.isNotEmpty()) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                items(tempat.tags.size) { i ->
                                    Text(
                                        text = tempat.tags[i],
                                        modifier = Modifier
                                            .background(Blue4, RoundedCornerShape(12.dp))
                                            .padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 12.sp,
                                        fontFamily = poppinsFont
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Tentang
                        Text("Tentang", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = poppinsFont, color = Blue3)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = tempat.deskripsi.ifBlank { "Deskripsi tidak tersedia" },
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Jam operasional
                        Text("Jam Operasional", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = poppinsFont, color = Blue3)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Blue4.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Buka", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = if (tempat.open.isNotBlank() && tempat.close.isNotBlank())
                                    "${tempat.open} – ${tempat.close}"
                                else "Setiap hari",
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Ulasan section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Ulasan", fontFamily = poppinsFont, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Blue3)
                            TextButton(onClick = { showAddReviewDialog = true }) {
                                Text("+ Tambah", color = Blue3, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isLoadingReview) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else if (reviews.isEmpty()) {
                            Text(
                                "Belum ada ulasan. Jadilah yang pertama!",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            reviews.forEach { review ->
                                ReviewCard(
                                    review = review,
                                    currentUserId = reviewViewModel.currentUserId,
                                    onEdit = {
                                        editingReview = review
                                        showEditReviewDialog = true
                                    },
                                    onDelete = {
                                        reviewViewModel.deleteReview(review.id, tempat.id)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Ulasan berhasil dihapus")
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Tombol Tambah Ulasan
                        Button(
                            onClick = { showAddReviewDialog = true },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green)
                        ) {
                            Text("Tambahkan Ulasan", color = Color.White, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun ReviewCard(
        review: Review,
        currentUserId: String? = null,
        onEdit: (() -> Unit)? = null,
        onDelete: (() -> Unit)? = null
    ) {
        val isOwner = currentUserId != null && review.userId == currentUserId

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Blue4),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.userName.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                            fontWeight = FontWeight.Bold,
                            color = Blue3,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = review.userName.ifBlank { "Pengguna" }, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Row {
                            repeat(5) { i ->
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = if (i < review.rating) Color(0xFFFFC107) else Color.LightGray,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = review.createdAt.take(10),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                if (review.comment.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = review.comment, fontSize = 13.sp, color = Color.DarkGray)
                }

                // Tombol Edit & Hapus (hanya untuk review milik user sendiri)
                if (isOwner) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onEdit?.invoke() }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Edit Ulasan",
                                tint = Blue3,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = { onDelete?.invoke() }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Hapus Ulasan",
                                tint = Color.Red.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AddReviewDialog(
        onDismiss: () -> Unit,
        onSubmit: (Int, String) -> Unit,
        initialRating: Int = 5,
        initialComment: String = "",
        dialogTitle: String = "Tulis Ulasan"
    ) {
        var rating by remember { mutableIntStateOf(initialRating) }
        var comment by remember { mutableStateOf(initialComment) }

        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        dialogTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = poppinsFont,
                        color = Blue3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pilih rating bintang
                    Text("Rating", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        (1..5).forEach { star ->
                            IconButton(onClick = { rating = star }) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "$star bintang",
                                    tint = if (star <= rating) Color(0xFFFFC107) else Color.LightGray,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Tulis komentar...") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        maxLines = 5,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Batal")
                        }
                        Button(
                            onClick = { onSubmit(rating, comment) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Green),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Kirim", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Legacy composable kept for compatibility (unused)
    @Composable
    fun Card(title: String, username: String, rating: Int, content: String) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .padding(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = username, fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    for (i in 1..5) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = content, fontSize = 14.sp)
            }
        }
    }
