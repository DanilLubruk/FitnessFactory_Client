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

    suspend fun addClient(userId: String) {
        val documentReference = getCollection().document(userId)
        val personnel = Personnel()
        personnel.userId = userId
        documentReference.set(personnel).await()
    }

    suspend fun isOwnerClientEntity(userId: String): Boolean {
        val entitiesAmount =
            getEntitiesAmount(getCollection().whereEqualTo(Personnel.USER_ID_FIELD, userId))

        return entitiesAmount > 0
    }
}