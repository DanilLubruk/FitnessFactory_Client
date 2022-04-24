package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Session
import com.google.firebase.firestore.FieldValue

class SessionsRepository: BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getSessionsCollection()

    suspend fun getRemoveClientBatch(sessionId: String, clientId: String) =
        getFirestore()
            .batch()
            .update(
                getCollection().document(sessionId),
                Session.CLIENTS_IDS_FIELD,
                FieldValue.arrayRemove(clientId))

    suspend fun getAddClientBatch(sessionId: String, clientId: String) =
        getFirestore()
            .batch()
            .update(getCollection().document(sessionId),
                Session.CLIENTS_IDS_FIELD,
                FieldValue.arrayUnion(clientId))


}