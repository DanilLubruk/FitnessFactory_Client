package com.example.fitnessfactory_client.ui.screens.sessionTypesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.dataListeners.SessionTypesDataListener
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SessionTypesScreenViewModel
@Inject constructor(private val sessionTypesDataListener: SessionTypesDataListener):
    ViewModel() {

    private val mutableTypesListState = MutableStateFlow<SessionTypesListState>(SessionTypesListState.Loading)
    val typesListState: StateFlow<SessionTypesListState> = mutableTypesListState

    fun startDataListener() {
        viewModelScope.launch {
            sessionTypesDataListener.startDataListener()
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect {
                    mutableTypesListState.emit(SessionTypesListState.Loaded(ArrayList(it)))
                }
        }
    }
}