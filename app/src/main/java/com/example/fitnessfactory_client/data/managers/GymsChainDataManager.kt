package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.beans.GymsChainData
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.data.repositories.OwnerCoachRepository
import com.example.fitnessfactory_client.data.repositories.OwnerGymsRepository
import com.example.fitnessfactory_client.data.repositories.SessionTypesRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import com.example.fitnessfactory_client.utils.ResUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
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
            val gymsList = ArrayList<Gym>()
            gymsList.add(Gym.getNoFilterEntity())
            gymsList.addAll(ownerGymsRepository.getGyms())

            val sessionTypesList = ArrayList<SessionType>()
            sessionTypesList.add(SessionType.getNoFilterEntity())
            sessionTypesList.addAll(sessionTypesRepository.getSessionTypes())

            val coaches = ownerCoachRepository.getPersonnelList()
            val coachUsers = usersRepository.getAppUsersByPersonnel(coaches)
            val coachesDataList = ArrayList<CoachData>()
            coachesDataList.add(CoachData.getNoFilterEntity())
            coachesDataList.addAll(getCoachesData(coachUsers = coachUsers, coaches = coaches))

            val gymsChainData = GymsChainData(
                gyms = gymsList,
                sessionTypes = sessionTypesList,
                coaches = coachesDataList
            )

            emit(gymsChainData)
        }

    private fun getCoachesData(coachUsers: List<AppUser>, coaches: List<Personnel>): List<CoachData> {
        if (coachUsers.size != coaches.size) {
            throw Exception(ResUtils.getString(R.string.message_error_coach_users))
        }

        val coachesDataList = ArrayList<CoachData>()
        coachUsers.forEachIndexed { index, coachUser ->
            coachesDataList.add(CoachData(coachUser = coachUser, coach = coaches[index]))
        }

        return coachesDataList
    }
}