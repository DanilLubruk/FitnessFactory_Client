package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.tasks.await

class OwnerClientRepository : OwnerPersonnelRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerClientsCollection()

    override fun getNotUniqueEmailMessage(): String =
        StringUtils.getMessageErrorClientsSameEmail()

    suspend fun addClient(usersEmail: String) {
        val documentReference = getCollection().document()
        val personnel = Personnel()
        personnel.id = documentReference.id
        personnel.userEmail = usersEmail
        documentReference.set(personnel).await()
    }

    suspend fun isOwnerClientEntity(usersEmail: String): Boolean {
        val entitiesAmount =
            getEntitiesAmount(getCollection().whereEqualTo(Personnel.USER_EMAIL_FIELD, usersEmail))

        return entitiesAmount > 0
    }
}