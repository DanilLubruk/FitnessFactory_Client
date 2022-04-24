package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.AccessEntry
import com.example.fitnessfactory_client.data.models.UsersAccessEntry
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ClientsAccessRepository : BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getClientsAccessCollection()

    suspend fun addClient(ownerId: String, userId: String) {
        val documentReference = getCollection().document(userId)
        val accessEntry = AccessEntry()
        accessEntry.ownerId = ownerId
        accessEntry.usersId = userId
        documentReference.set(accessEntry).await()
    }

    suspend fun hasClientAccessEntity(ownerId: String, userId: String): Boolean {
        val entitiesAmount =
            getEntitiesAmount(
            getCollection().whereEqualTo(AccessEntry.OWNER_ID_FIELD, ownerId)
                .whereEqualTo(AccessEntry.USERS_ID_FIELD, userId)
        )

        return entitiesAmount > 0
    }

    suspend fun getInvitedOwnersIds(userId: String): List<String> {
        val ownersIds: ArrayList<String> = ArrayList()

        getQuerySnapshot(QueryBuilder().whereUserIdEquals(userId = userId).build())
            .toObjects(UsersAccessEntry::class.java)
            .forEach {
                ownersIds.add(it.ownerId)
            }

        return ownersIds
    }

    private inner class QueryBuilder {

        private var query: Query = getCollection()

        fun whereUserIdEquals(userId: String): QueryBuilder {
            query = query.whereEqualTo(UsersAccessEntry.USER_ID_FIELD, userId)
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