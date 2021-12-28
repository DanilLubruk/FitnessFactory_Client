package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.firestoreCollections.UsersCollection
import com.example.fitnessfactory_client.data.models.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class UsersRepository: BaseRepository() {

    override fun getRoot(): String =
        UsersCollection.getUsersCollection()

    suspend fun checkUserRegistered(usersEmail: String): Boolean {
        val usersWithEmailAmount = getEntitiesAmount(QueryBuilder().whereEmailEquals(usersEmail).build())

        return usersWithEmailAmount > 0
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