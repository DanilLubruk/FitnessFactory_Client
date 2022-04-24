package com.example.fitnessfactory_client.ui.screens.personalInfoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.AppPrefs
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonalInfoScreenViewModel @Inject constructor(private val usersRepository: UsersRepository) :
    ViewModel() {

    private val mutableUserInfoSharedFlow: MutableSharedFlow<AppUser> = MutableSharedFlow()
    val userInfoSharedFlow: SharedFlow<AppUser> = mutableUserInfoSharedFlow.asSharedFlow()

    fun fetchUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val appUser = usersRepository.getAppUserByEmail(AppPrefs.currentUserEmail().value)
            mutableUserInfoSharedFlow.emit(appUser)
        }
    }

    suspend fun isModified(userId: String, userName: String, userEmail: String): Boolean {
        val appUser = AppUser.newValue(id = userId, name = userName, email = userEmail)
        val dbUser = usersRepository.getAppUserById(userId)

        return !appUser.equals(dbUser)
    }

    suspend fun save(userId: String, userName: String, userEmail: String): Boolean {
        return try {
            usersRepository.updateUserData(AppUser.newValue(id = userId, name = userName, email = userEmail))
        } catch (throwable: Exception) {
            throwable.printStackTrace()
            GuiUtils.showMessage(throwable.localizedMessage)
            false;
        }
    }
}