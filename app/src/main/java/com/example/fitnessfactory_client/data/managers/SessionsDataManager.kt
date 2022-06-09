package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.data.repositories.*
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import com.example.fitnessfactory_client.utils.ResUtils
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
    private val sessionTypesRepository: SessionTypesRepository,
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
                val session: Session
                sessionsRepository.getSession(sessionId = sessionId).let {
                    if (it == null) {
                        throw Exception("NULL")
                    }
                    session = it
                };

                val sessionType: SessionType
                sessionTypesRepository.getSessionType(session.sessionTypeId).let {
                    if (it == null) {
                        throw Exception("NULL")
                    }
                    sessionType = it
                }

                if (session.clientsIds == null || sessionType.peopleAmount <= session.clientsIds?.size ?: 0) {
                    throw Exception(ResUtils.getString(R.string.message_session_full))
                }

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