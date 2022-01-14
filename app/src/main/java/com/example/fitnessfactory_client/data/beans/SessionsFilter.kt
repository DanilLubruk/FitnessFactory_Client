package com.example.fitnessfactory_client.data.beans

import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.data.models.SessionType

class SessionsFilter(
    var gym: Gym,
    var sessionType: SessionType,
    var coachData: CoachData,
    var doFilter: Boolean = true
) {

    companion object {
        fun getNoFilterEntity(): SessionsFilter =
            SessionsFilter(
                Gym(),
                SessionType(),
                coachData = CoachData(coach = Personnel(), coachUser = AppUser()),
                doFilter = false
            )

        fun getCoachFilteredEntity(coachData: CoachData): SessionsFilter =
            SessionsFilter(gym = Gym.getNoFilterEntity(), sessionType = SessionType.getNoFilterEntity(), coachData = coachData)
    }
}