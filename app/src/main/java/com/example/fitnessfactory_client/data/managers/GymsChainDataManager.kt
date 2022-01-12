package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.beans.GymsChainData
import com.example.fitnessfactory_client.data.repositories.OwnerCoachRepository
import com.example.fitnessfactory_client.data.repositories.OwnerGymsRepository
import com.example.fitnessfactory_client.data.repositories.SessionTypesRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GymsChainDataManager
@Inject constructor(
    private val ownerGymsRepository: OwnerGymsRepository,
    private val sessionTypesRepository: SessionTypesRepository,
    private val ownerCoachRepository: OwnerCoachRepository,
    private val usersRepository: UsersRepository
) {

    fun getGymsChainData(): Flow<GymsChainData> =
        flow {
            val gymsList = ownerGymsRepository.getGyms()
            val sessionTypesList = sessionTypesRepository.getSessionTypes()

            val coachesEmails = ownerCoachRepository.getPersonnelEmails()
            val coachUsers = usersRepository.getAppUsersByEmails(coachesEmails)

            val gymsChainData = GymsChainData(
                gyms = gymsList,
                sessionTypes = sessionTypesList,
                coaches = coachUsers
            )

            emit(gymsChainData)
        }
}