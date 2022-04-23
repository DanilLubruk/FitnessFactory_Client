package com.example.fitnessfactory_client.ui.screens.personalInfoScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.AppPrefs
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonalInfoScreenViewModel @Inject constructor(private val usersRepository: UsersRepository) : ViewModel() {

    private val mutableUserInfoSharedFlow: MutableSharedFlow<AppUser> = MutableSharedFlow()
    val userInfoSharedFlow: SharedFlow<AppUser> = mutableUserInfoSharedFlow.asSharedFlow()

    private val mutableDbUserSharedFlow: MutableSharedFlow<AppUser> = MutableSharedFlow()
    val dbUserSharedFlow: SharedFlow<AppUser> = mutableUserInfoSharedFlow.asSharedFlow()

    fun fetchUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val appUser = usersRepository.getAppUserByEmail(AppPrefs.currentUserEmail().value)
            mutableUserInfoSharedFlow.emit(appUser)
        }
    }

    fun fetchDbUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val appUser = usersRepository.getAppUserById(userId)
            mutableDbUserSharedFlow.emit(appUser)
        }
    }
}