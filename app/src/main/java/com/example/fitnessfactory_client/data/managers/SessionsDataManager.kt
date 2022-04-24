package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.repositories.ClientSessionsRepository
import com.example.fitnessfactory_client.data.repositories.OwnerClientRepository
import com.example.fitnessfactory_client.data.repositories.SessionsRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SessionsDataManager
@Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val ownerClientRepository: OwnerClientRepository,
    private val sessionsRepository: SessionsRepository,
    private val clientSessionsRepository: ClientSessionsRepository,
    private val usersRepository: UsersRepository,
) {

    fun removeClientFromSession(sessionId: String): Flow<Unit> =
        flow {
            firebaseAuthManager.getCurrentUserEmail()?.let { clientEmail ->
                val appUser = usersRepository.getAppUserByEmail(clientEmail)
                val clientId = appUser.id
                var writeBatch = sessionsRepository.getRemoveClientBatch(
                    sessionId = sessionId,
                    clientId = clientId
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
                val appUser = usersRepository.getAppUserByEmail(clientEmail)
                val clientId = appUser.id
                var writeBatch = sessionsRepository.getAddClientBatch(
                    sessionId = sessionId,
                    clientId = clientId
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