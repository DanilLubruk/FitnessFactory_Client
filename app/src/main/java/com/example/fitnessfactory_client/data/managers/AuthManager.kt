package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.beans.OwnersData
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.repositories.OwnersRepository
import com.example.fitnessfactory_client.data.repositories.ClientsAccessRepository
import com.example.fitnessfactory_client.data.repositories.OwnerClientRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val usersRepository: UsersRepository,
    private val ownersRepository: OwnersRepository,
    private val clientsAccessRepository: ClientsAccessRepository,
    private val ownerClientRepository: OwnerClientRepository
) {

    fun registerClient(ownerId: String): Flow<Unit> =
        flow {
            firebaseAuthManager.getCurrentUserEmail()?.let { usersEmail ->
                val appUser = usersRepository.getAppUserByEmail(usersEmail = usersEmail)
                val isClientAccessEntry = clientsAccessRepository.hasClientAccessEntity(
                    ownerId = ownerId,
                    userId = appUser.id
                )
                if (!isClientAccessEntry) {
                    clientsAccessRepository.addClient(ownerId = ownerId, userId = appUser.id)
                }

                val isOwnerClientEntry =
                    ownerClientRepository.isOwnerClientEntity(userId = appUser.id)
                if (!isOwnerClientEntry) {
                    ownerClientRepository.addClient(userId = appUser.id)
                }
            }

            emit(Unit)
        }

    fun handleSignInResult(googleSignInAccount: GoogleSignInAccount): Flow<AppUser> =
        flow {
            val appUser = firebaseAuthManager.handleSignIn(googleSignInAccount)
            usersRepository.registerUserAsync(appUser = appUser)

            emit(appUser)
        }

    fun getAuthOwnersData(userId: String): Flow<OwnersData> =
        flow {
            val ownersData = OwnersData()

            ownersData.allOwnersList = ownersRepository.getAllOwners()
            val ownersIds = clientsAccessRepository.getInvitedOwnersIds(userId = userId)
            ownersData.invitedOwnersList = ownersRepository.getOwnersByIds(ownersIds = ownersIds)

            emit(ownersData)
        }
}