package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.repositories.OwnerCoachRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoachesAccessManager
@Inject constructor(
    private val ownerCoachRepository: OwnerCoachRepository,
    private val usersRepository: UsersRepository
) {

    fun getCoachesUsersByIds(coachesIds: List<String>): Flow<List<AppUser>> =
        flow {
            val coachesEmails = ownerCoachRepository.getPersonnelEmailsByIds(coachesIds)
            val coachUsers = usersRepository.getAppUsersByEmails(coachesEmails)

            emit(coachUsers)
        }
}