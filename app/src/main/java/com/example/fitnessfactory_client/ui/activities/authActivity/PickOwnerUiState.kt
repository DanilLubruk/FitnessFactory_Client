package com.example.fitnessfactory_client.ui.activities.authActivity

import com.example.fitnessfactory_client.data.beans.OwnersData
import com.example.fitnessfactory_client.data.models.Owner

sealed class PickOwnerUiState {
    data class Success(val ownersData: OwnersData): PickOwnerUiState()
    data class Error(var exception: Throwable): PickOwnerUiState()
    class Loading: PickOwnerUiState()
}