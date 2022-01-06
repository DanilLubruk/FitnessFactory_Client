package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.OrganisationData
import com.example.fitnessfactory_client.data.models.Owner
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class OwnersRepository: BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnersCollection()

    suspend fun getOwnersByIds(ownersIds: List<String>): List<Owner> {
        val ownersList = ArrayList<Owner>()

        if (ownersIds.isEmpty()) {
            return ownersList
        }

        getQuerySnapshot(QueryBuilder().whereIdInArray(ownersIds = ownersIds).build()).documents
            .forEach { document ->
                document.toObject(Owner::class.java)?.let {
                    val owner = it
                    owner.organisationName =
                        getCollection()
                            .document(document.id)
                            .collection(document.id)
                            .document(FirestoreCollections.organisationDataCollectionValue)
                            .get().await()
                            .get(OrganisationData.NAME_FIELD).toString()
                    ownersList.add(owner)
                }
            }

        return ownersList
    }

    suspend fun getAllOwners(): List<Owner> {
        val allOwnersList = ArrayList<Owner>()

        getQuerySnapshot(getCollection()).documents
            .forEach { document ->
                document.toObject(Owner::class.java)?.let {
                    val owner = it
                    owner.organisationName =
                        getCollection()
                        .document(document.id)
                            .collection(document.id)
                            .document(FirestoreCollections.organisationDataCollectionValue)
                            .get().await()
                            .get(OrganisationData.NAME_FIELD).toString()
                    allOwnersList.add(owner)
                }
            }

        return allOwnersList
    }

    private inner class QueryBuilder {

        private var query: Query = getCollection()

        fun whereIdInArray(ownersIds: List<String>): QueryBuilder {
            query = query.whereIn(Owner.ID_FIELD, ownersIds)
            return this
        }

        fun build(): Query =
            query
    }
}