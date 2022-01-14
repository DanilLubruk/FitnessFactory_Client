package com.example.fitnessfactory_client.ui.screens.sessionTypesScreen

import com.example.fitnessfactory_client.data.models.SessionType

sealed class SessionTypesListState {
    data class Loaded(val sessionTypes: ArrayList<SessionType>): SessionTypesListState()
    object Loading: SessionTypesListState()
}