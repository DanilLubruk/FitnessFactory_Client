package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.repositories.OwnerCoachRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoachesDataManager
@Inject constructor(
    private val ownerCoachRepository: OwnerCoachRepository,
    private val usersRepository: UsersRepository
) {

    fun getCoachesByGymId(gymIds: String): Flow<List<AppUser>> =
        flow {
            val coaches = ownerCoachRepository.getCoachesByGymId(gymId = gymIds)
            val coachUsers = usersRepository.getAppUsersByPersonnel(coaches)

            emit(coachUsers)
        }

}