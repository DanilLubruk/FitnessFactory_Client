package com.example.fitnessfactory_client.ui.screens.homeScreen

import com.example.fitnessfactory_client.data.models.Session
import java.util.*

sealed class SessionsListState {
    data class Loaded(val sessionsList: List<Date>): SessionsListState()
    data class Error(val throwable: Throwable): SessionsListState()
    object Loading : SessionsListState()
}
