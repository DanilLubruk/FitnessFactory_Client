package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.repositories.SessionViewRepository
import javax.inject.Inject

class SessionsDataManager @Inject constructor(private val sessionViewRepository: SessionViewRepository,
) {
}