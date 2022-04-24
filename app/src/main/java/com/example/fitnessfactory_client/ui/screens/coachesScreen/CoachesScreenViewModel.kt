package com.example.fitnessfactory_client.ui.screens.coachesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.dataListeners.CoachesListListener
import com.example.fitnessfactory_client.data.managers.GymsChainDataManager
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.repositories.OwnerCoachRepository
import com.example.fitnessfactory_client.data.repositories.OwnerGymsRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoachesScreenViewModel
@Inject constructor(
    private val coachesListListener: CoachesListListener,
    private val gymsChainDataManager: GymsChainDataManager,
    private val usersRepository: UsersRepository,
    private val gymsRepository: OwnerGymsRepository,
    private val ownerPersonnelRepository: OwnerCoachRepository,
) : ViewModel() {

    private val mutableCoachesListState =
        MutableStateFlow<CoachesListState>(CoachesListState.Loading)
    val coachesListState: StateFlow<CoachesListState> = mutableCoachesListState

    private val mutableGymsList = MutableSharedFlow<ArrayList<Gym>>()
    val gymsList: SharedFlow<ArrayList<Gym>> = mutableGymsList

    private val mutableCoachForFilter = MutableSharedFlow<CoachData>()
    val coachForFilter: SharedFlow<CoachData> = mutableCoachForFilter

    fun startDataListener() {
        viewModelScope.launch {
            coachesListListener.startDataListener()
                .map { usersRepository.getAppUsersByPersonnel(it) }
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect {
                    mutableCoachesListState.emit(CoachesListState.Loaded(it))
                }
        }
    }

    fun fetchGymsList(coachUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val coach = ownerPersonnelRepository.getPersonnelById(coachUserId)
                val gyms = gymsRepository.getCoachGyms(coach = coach)
                mutableGymsList.emit(ArrayList(gyms))
            } catch (throwable: Exception) {
                throwable.printStackTrace()
                throwable.localizedMessage?.let { GuiUtils.showMessage(it) }
            }
        }
    }

    fun fetchCoachForFilter(coachUserId: String) {
        viewModelScope.launch {
            gymsChainDataManager.getCoachDataById(coachUserId = coachUserId)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect {
                    mutableCoachForFilter.emit(it)
                }
        }
    }
}