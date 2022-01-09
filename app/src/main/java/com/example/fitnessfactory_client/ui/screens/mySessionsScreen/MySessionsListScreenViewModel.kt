package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.dataListeners.DaysSessionsListListener
import com.example.fitnessfactory_client.data.repositories.SessionViewRepository
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MySessionsListScreenViewModel
@Inject constructor(
    private val sessionViewRepository: SessionViewRepository,
    private val daysSessionsListListener: DaysSessionsListListener
) : ViewModel() {

    private val mutableSessionsListState = MutableStateFlow<SessionsListState>(SessionsListState.Loading)
    val sessionsListState: StateFlow<SessionsListState> = mutableSessionsListState

    fun startDataListener(date: Date) {
        viewModelScope.launch {
            daysSessionsListListener.startDataListener(date = date)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                    mutableSessionsListState.emit(SessionsListState.Error(throwable = throwable))
                }
                .collect { sessions ->
                    val sessionViews = sessionViewRepository.getSessionViewsList(sessionsList = sessions)
                    mutableSessionsListState.emit(SessionsListState.Loaded(sessionViews))
                }
        }
    }
}