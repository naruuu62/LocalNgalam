package com.example.localngalam.autentikasi

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.model.UserData
import com.example.localngalam.model.saveUserDataFireStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class autentikasiViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val lloginState = MutableStateFlow<Boolean?>(null)
    val loginState: StateFlow<Boolean?> get() = lloginState
    lateinit var googleSignInClient: GoogleSignInClient
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> get() = _userData

    fun initGoogleSignInClient(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("497053976903-62igeq8ktk2iqoa06mp68vh94gihgg1v.apps.googleusercontent.com") // Ganti dengan ID Anda
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun loginGoogle(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val signInIntent = googleSignInClient.signInIntent
        onSuccess()
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    lloginState.value = task.isSuccessful
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        val user = auth.currentUser
                        // Simpan data ke Firestore
                        val userData = UserData(uid = uid,namaLengkap = user?.displayName.toString(), noTelepon = user?.phoneNumber.toString(), email = user?.email.toString())
                        saveUserDataFireStore(userData)

                        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                        db.collection("users").document(uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Data user berhasil disimpan")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Gagal menyimpan data user: ${e.message}")
                            }
                        getUserDataFromFirestore(uid)
                    }
                }
                .addOnFailureListener { exception ->
                    viewModelScope.launch {
                        lloginState.value = false
                    }
                }
        }
    }

    fun register(email: String, password: String, namaLengkap: String, noTelepon: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null){
                            val user = auth.currentUser
                            val userData = UserData(uid = uid.toString(),namaLengkap = namaLengkap, noTelepon = noTelepon, email = email)
                            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                            db.collection("users").document(uid)
                                .set(userData)
                            getUserDataFromFirestore(uid)
                        }
                        lloginState.value = true
                    } else {
                        lloginState.value = false
                    }
                }
                .addOnFailureListener {
                    lloginState.value = false
                }
        }
    }




    fun login(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null){
                            Log.d("Login", "Login berhasil, mengambil data user dari Firestore: $uid")
                            getUserDataFromFirestore(uid)
                        }
                        lloginState.value = task.isSuccessful
                    } else {
                        lloginState.value = false
                    }
                }
                .addOnFailureListener { exception ->
                    viewModelScope.launch {
                        lloginState.value = false
                    }
                }
        }
    }


    fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Autentikasi", "Email reset password terkirim")
            } else {
                Log.e("Autentikasi", "Gagal mengirim email reset password")
            }
        }
    }



    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
        if(::googleSignInClient.isInitialized){
        googleSignInClient.signOut().addOnCompleteListener {
            lloginState.value = false
            Log.d("Autentikasi", "Logout berhasil")
        }
            }
        else{
            Log.e("Autentikasi", "Google Sign-In client belum diinisialisasi")
        }
        }

    private fun getUserDataFromFirestore(uid: String) {
        Log.d("Firestore", "Mengambil data untuk UID: $uid")
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(UserData::class.java)
                    _userData.value = user
                    Log.d("Firestore", "Data user ditemukan: $user")
                } else {
                    Log.e("Firestore", "Data user tidak ditemukan")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Gagal mengambil data user: ${exception.message}")
            }
    }

    }

