package com.example.fitnessfactory_client.ui.screens.gymsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.dataListeners.GymsListListener
import com.example.fitnessfactory_client.data.managers.CoachesDataManager
import com.example.fitnessfactory_client.data.managers.GymsChainDataManager
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class GymsScreenViewModel
@Inject constructor(
    private val gymsListListener: GymsListListener,
    private val coachesDataManager: CoachesDataManager
) :
    ViewModel() {

    private val mutableGymsListState = MutableStateFlow<GymsListState>(GymsListState.Loading)
    val gymsListState: StateFlow<GymsListState> = mutableGymsListState

    private val mutableCoachesDataList = MutableSharedFlow<ArrayList<AppUser>>()
    val coachesDataList: SharedFlow<ArrayList<AppUser>> = mutableCoachesDataList

    fun startDataListener() {
        viewModelScope.launch {
            gymsListListener.startDataListener()
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect {
                    mutableGymsListState.emit(GymsListState.Loaded(ArrayList(it)))
                }
        }
    }

    fun fetchCoachesData(gymId: String) {
        viewModelScope.launch {
            coachesDataManager.getCoachesByGymId(gymIds = gymId)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect {
                    mutableCoachesDataList.emit(ArrayList(it))
                }
        }
    }
}