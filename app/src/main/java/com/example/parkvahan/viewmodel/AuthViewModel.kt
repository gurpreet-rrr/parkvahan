package com.example.parkvahan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkvahan.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth     : FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, role: UserRole) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            delay(500)
            _uiState.value = AuthUiState(isSuccess = true)
        }
    }

    fun register(name: String, email: String, password: String, role: UserRole) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            delay(500)
            _uiState.value = AuthUiState(isSuccess = true)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}