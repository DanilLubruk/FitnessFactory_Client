package com.example.fitnessfactory_client.data.beans

import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.data.models.SessionType

class SessionsFilter(var gym: Gym, var sessionType: SessionType, var coach: AppUser) {
}