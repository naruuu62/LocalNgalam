package com.example.localngalam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class autentikasi : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val lloginState = MutableStateFlow<Boolean?>(null)
    val loginState: StateFlow<Boolean?> = lloginState

    fun register(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    lloginState.value = task.isSuccessful
                }
                .addOnFailureListener { exception ->
                    viewModelScope.launch {
                        lloginState.value = false
                    }
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful == true) {
                        lloginState.value = true
                    } else {
                        lloginState.value = false
                    }
                }
        }
    }

}