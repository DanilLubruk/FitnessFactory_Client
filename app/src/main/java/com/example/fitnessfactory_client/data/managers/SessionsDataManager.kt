package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.repositories.ClientSessionsRepository
import com.example.fitnessfactory_client.data.repositories.OwnerClientRepository
import com.example.fitnessfactory_client.data.repositories.SessionsRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SessionsDataManager
@Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val ownerClientRepository: OwnerClientRepository,
    private val sessionsRepository: SessionsRepository,
    private val clientSessionsRepository: ClientSessionsRepository
) {

    fun removeClientFromSession(sessionId: String): Flow<Unit> =
        flow {
            firebaseAuthManager.getCurrentUserEmail()?.let { clientEmail ->
                val clientId = ownerClientRepository.getPersonnelIdByEmail(clientEmail)
                var writeBatch = sessionsRepository.getRemoveClientBatch(
                    sessionId = sessionId,
                    clientEmail = clientEmail
                )
                writeBatch = clientSessionsRepository.getRemoveSessionBatch(
                    writeBatch = writeBatch,
                    sessionId = sessionId,
                    clientId = clientId
                )
                writeBatch.commit()

                emit(Unit)
            }
        }

    fun addClientToSession(sessionId: String): Flow<Unit> =
        flow {
            firebaseAuthManager.getCurrentUserEmail()?.let { clientEmail ->
                var clientId= ownerClientRepository.getPersonnelIdByEmail(clientEmail)
                var writeBatch = sessionsRepository.getAddClientBatch(
                    sessionId = sessionId,
                    clientEmail = clientEmail
                )
                writeBatch = clientSessionsRepository.getAddSessionBatch(
                    writeBatch = writeBatch,
                    sessionId = sessionId,
                    clientId = clientId
                )
                writeBatch.commit()

                emit(Unit)
            }
        }
}