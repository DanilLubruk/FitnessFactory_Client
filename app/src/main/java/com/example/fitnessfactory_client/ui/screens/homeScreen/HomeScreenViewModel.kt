package com.example.fitnessfactory_client.ui.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.dataListeners.DaysSessionsListListener
import com.example.fitnessfactory_client.data.repositories.SessionViewRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.SessionViewsListState
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val sessionViewRepository: SessionViewRepository,
    private val daysSessionsListListener: DaysSessionsListListener
) : ViewModel() {

    private val mutableSessionsListState =
        MutableStateFlow<SessionViewsListState>(SessionViewsListState.Loading)
    val sessionViewsListState: StateFlow<SessionViewsListState> = mutableSessionsListState

    fun startDataListener(date: Date) {
        viewModelScope.launch {
            firebaseAuthManager.getCurrentUserEmail()?.let { usersEmail ->
                daysSessionsListListener.startDataListener(date = date, usersEmail = usersEmail)
                    .map { sessions ->
                        sessionViewRepository.getSessionViewsList(sessionsList = sessions)
                    }
                    .flowOn(Dispatchers.IO)
                    .catch { throwable ->
                        throwable.printStackTrace()
                        GuiUtils.showMessage(throwable.localizedMessage)
                        mutableSessionsListState.emit(SessionViewsListState.Error(throwable = throwable))
                    }
                    .collect { sessionViews ->
                        mutableSessionsListState.emit(SessionViewsListState.Loaded(sessionViews))
                    }
            }
        }
    }
}