package com.example.fitnessfactory_client.data.beans

import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.data.models.SessionType
import java.util.logging.Filter

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

        fun builder(): FilterBuilder =
            FilterBuilder()
    }

    class FilterBuilder {
        private var gym: Gym = Gym.getNoFilterEntity()
        private var sessionType: SessionType = SessionType.getNoFilterEntity()
        private var coachData: CoachData = CoachData.getNoFilterEntity()

        fun filterGym(gym: Gym): FilterBuilder {
            this.gym = gym
            return this
        }

        fun filterSessionType(sessionType: SessionType): FilterBuilder {
            this.sessionType = sessionType
            return this
        }

        fun filterCoach(coachData: CoachData): FilterBuilder {
            this.coachData = coachData
            return this
        }

        fun build(): SessionsFilter =
            SessionsFilter(gym = gym, sessionType = sessionType, coachData = coachData)
    }
}