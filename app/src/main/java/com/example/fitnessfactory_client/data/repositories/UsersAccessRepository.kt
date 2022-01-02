package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.UsersAccessEntry
import com.google.firebase.firestore.Query

class UsersAccessRepository : BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getUsersAccessCollection()

    suspend fun getInvitedOwnersIds(usersEmail: String): List<String> {
        val ownersIds: ArrayList<String> = ArrayList()

        getQuerySnapshot(QueryBuilder().whereUsersEmailEquals(usersEmail = usersEmail).build())
            .toObjects(UsersAccessEntry::class.java)
            .forEach {
                ownersIds.add(it.ownerId)
            }

        return ownersIds
    }

    private inner class QueryBuilder {

        private var query: Query = getCollection()

        fun whereUsersEmailEquals(usersEmail: String): QueryBuilder {
            query = query.whereEqualTo(UsersAccessEntry.USER_EMAIL_FIELD, usersEmail)
            return this
        }

        fun whereOwnerIdEquals(ownerId: String): QueryBuilder {
            query = query.whereEqualTo(UsersAccessEntry.OWNER_ID_FIELD, ownerId)
            return this
        }

        fun build(): Query =
            query
    }
}