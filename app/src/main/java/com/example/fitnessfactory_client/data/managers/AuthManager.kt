package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.beans.OwnersData
import com.example.fitnessfactory_client.data.repositories.OwnersRepository
import com.example.fitnessfactory_client.data.repositories.ClientsAccessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val ownersRepository: OwnersRepository,
    private val clientsAccessRepository: ClientsAccessRepository
) {

    fun getAuthOwnersData(usersEmail: String): Flow<OwnersData> =
        flow {
            val ownersData = OwnersData()

            ownersData.allOwnersList = ownersRepository.getAllOwners()
            val ownersIds = clientsAccessRepository.getInvitedOwnersIds(usersEmail = usersEmail)
            ownersData.invitedOwnersList = ownersRepository.getOwnersByIds(ownersIds = ownersIds)

            emit(ownersData)
        }
}