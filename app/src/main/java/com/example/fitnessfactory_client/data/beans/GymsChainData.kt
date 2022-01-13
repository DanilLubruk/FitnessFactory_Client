package com.example.fitnessfactory_client.data.beans

import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.SessionType

class GymsChainData(var gyms: List<Gym>,
                    var sessionTypes: List<SessionType>,
                    var coaches: List<CoachData>) {

    companion object {
        fun getBlankObject(): GymsChainData =
            GymsChainData(ArrayList(), ArrayList(), ArrayList())
    }
}