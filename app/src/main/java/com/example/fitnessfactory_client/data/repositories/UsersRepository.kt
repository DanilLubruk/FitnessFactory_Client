package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.AppUser
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class UsersRepository: BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getUsersCollection()

    fun registerUserOperation(usersName: String, usersEmail: String): Flow<Unit> =
        flow {
            val isRegistered = checkUserRegistered(usersEmail = usersEmail)
            if (!isRegistered) {
                registerUser(name = usersName, email = usersEmail)
            }

            emit(Unit)
        }

    suspend fun checkUserRegistered(usersEmail: String): Boolean {
        val usersWithEmailAmount = getEntitiesAmount(QueryBuilder().whereEmailEquals(usersEmail).build())

        return usersWithEmailAmount > 0
    }

    suspend fun registerUser(name: String, email: String) {
        val documentReference = getCollection().document();
        val appUser = AppUser()
        appUser.id = documentReference.id
        appUser.name = name
        appUser.email = email

        documentReference.set(appUser).await()
    }

    private inner class QueryBuilder {

        private var query: Query = getCollection()

        fun whereEmailEquals(email: String): QueryBuilder {
            query = query.whereEqualTo(AppUser.EMAIL_FIELD, email)
            return this
        }

        fun build(): Query =
            query
    }
}