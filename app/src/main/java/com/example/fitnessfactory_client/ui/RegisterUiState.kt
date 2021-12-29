package com.example.fitnessfactory_client.ui

sealed class RegisterUiState {
    class Success: RegisterUiState()
    data class Error(var exception: Throwable): RegisterUiState()
}