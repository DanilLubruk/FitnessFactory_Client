package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.dataListeners.DaysSessionsListListener
import com.example.fitnessfactory_client.data.managers.SessionsDataManager
import com.example.fitnessfactory_client.data.repositories.SessionViewRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MySessionsListScreenViewModel
@Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val sessionViewRepository: SessionViewRepository,
    private val daysSessionsListListener: DaysSessionsListListener,
    private val sessionsDataManager: SessionsDataManager
) : ViewModel() {

    private val mutableSessionsListState = MutableStateFlow<SessionsListState>(SessionsListState.Loading)
    val sessionsListState: StateFlow<SessionsListState> = mutableSessionsListState

    fun startDataListener(date: Date) {
        viewModelScope.launch {
            firebaseAuthManager.getCurrentUserEmail()?.let { usersEmail ->
                daysSessionsListListener.startDataListener(date = date, usersEmail = usersEmail)
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

    fun unsubscribeFromSession(sessionId: String) {
        viewModelScope.launch {
            sessionsDataManager.removeClientFromSession(sessionId = sessionId)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect()
        }
    }
}