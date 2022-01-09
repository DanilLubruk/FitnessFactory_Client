package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Session
import com.google.firebase.firestore.FieldValue

class SessionsRepository: BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getSessionsCollection()

    suspend fun getRemoveClientBatch(sessionId: String, clientEmail: String) =
        getFirestore()
            .batch()
            .update(
                getCollection().document(sessionId),
                Session.CLIENTS_EMAILS_FIELD,
                FieldValue.arrayRemove(clientEmail))


}