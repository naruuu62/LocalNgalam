package com.example.localngalam.autentikasiViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localngalam.data.UserData
import com.example.localngalam.data.saveUserDataFireStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class autentikasi : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val lloginState = MutableStateFlow<Boolean?>(null)
    val loginState: StateFlow<Boolean?> get() = lloginState
    private lateinit var googleSignInClient: GoogleSignInClient



    private var savedOobCode: String? = null // Untuk menyimpan oobCode


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
                        if (uid != null) {
                            // Simpan data ke Firestore
                            val userData = UserData(uid = uid, namaLengkap = namaLengkap, noTelepon = noTelepon, email = email)
                            saveUserDataFireStore(userData)
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
                    lloginState.value = task.isSuccessful
                    {

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
        googleSignInClient.signOut()
        lloginState.value = false
    }
}