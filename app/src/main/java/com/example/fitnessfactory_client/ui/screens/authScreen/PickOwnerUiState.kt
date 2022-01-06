package com.example.fitnessfactory_client.ui.screens.authScreen

import com.example.fitnessfactory_client.data.beans.OwnersData

sealed class PickOwnerUiState {
    data class Success(val ownersData: OwnersData): PickOwnerUiState()
    data class Error(var exception: Throwable): PickOwnerUiState()
    class Loading: PickOwnerUiState()
}