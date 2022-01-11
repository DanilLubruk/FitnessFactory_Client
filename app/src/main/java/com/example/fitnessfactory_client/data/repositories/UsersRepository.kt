package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Owner
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class UsersRepository : BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getUsersCollection()

    suspend fun getAppUsersByEmails(usersEmails: List<String>): List<AppUser> {
        if (usersEmails.isEmpty()) {
            return ArrayList()
        }

        return getQuerySnapshot(
            getCollection()
                .whereIn(AppUser.EMAIL_FIELD, usersEmails)
        )
            .toObjects(AppUser::class.java)
    }


    suspend fun registerUserAsync(appUser: AppUser) {
        val isRegistered = checkUserRegistered(usersEmail = appUser.email)
        if (!isRegistered) {
            registerUser(appUser = appUser)
        }
    }

    private suspend fun checkUserRegistered(usersEmail: String): Boolean {
        val usersWithEmailAmount =
            getEntitiesAmount(QueryBuilder().whereEmailEquals(usersEmail).build())

        return usersWithEmailAmount > 0
    }

    private suspend fun registerUser(appUser: AppUser) {
        val documentReference = getCollection().document();
        appUser.id = documentReference.id

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