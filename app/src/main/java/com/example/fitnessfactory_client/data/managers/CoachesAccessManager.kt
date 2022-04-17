package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.repositories.OwnerCoachRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import javax.inject.Inject

class CoachesAccessManager
@Inject constructor(
    private val ownerCoachRepository: OwnerCoachRepository,
    private val usersRepository: UsersRepository
) {

    suspend fun getCoachesUsersByEmails(coachesEmails: List<String>): List<AppUser> =
        usersRepository.getAppUsersByEmails(coachesEmails)
}