package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.SessionType
import kotlinx.coroutines.tasks.await

class SessionTypesRepository : BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getSessionTypesCollection()

    suspend fun getSessionTypes(): List<SessionType> =
        getQuerySnapshot(getCollection()).toObjects(SessionType::class.java)

    suspend fun getSessionType(sessionTypeId: String) =
        getCollection().document(sessionTypeId).get().await().toObject(SessionType::class.java);
}