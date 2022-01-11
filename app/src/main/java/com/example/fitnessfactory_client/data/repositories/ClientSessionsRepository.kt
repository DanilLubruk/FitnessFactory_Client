package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.UsersSession
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch

class ClientSessionsRepository : BaseRepository() {

    private fun getSessionsDocument(sessionId: String, clientId: String): DocumentReference =
        getCollection(clientId).document(sessionId)

    private fun getCollection(clientId: String): CollectionReference =
        getFirestore().collection(FirestoreCollections.getClientSessionsCollection(clientId))

    suspend fun getRemoveSessionBatch(
        writeBatch: WriteBatch,
        sessionId: String,
        clientId: String
    ) =
        writeBatch.delete(getSessionsDocument(sessionId = sessionId, clientId = clientId))

    suspend fun getAddSessionBatch(
        writeBatch: WriteBatch,
        sessionId: String, clientId: String
    ) =
        writeBatch.set(
            getSessionsDocument(sessionId = sessionId, clientId = clientId),
            UsersSession(sessionId = sessionId)
        )
}