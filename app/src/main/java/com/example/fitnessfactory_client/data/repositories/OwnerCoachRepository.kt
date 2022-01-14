package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.utils.StringUtils

class OwnerCoachRepository : OwnerPersonnelRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerCoachesCollection()

    override fun getNotUniqueEmailMessage(): String =
        StringUtils.getMessageErrorCoachesSameEmail()

    suspend fun getCoachesByGymId(gymId: String): List<Personnel> =
        getQuerySnapshot(
            getCollection().whereArrayContains(
                Personnel.GYMS_ARRAY_FIELD,
                gymId
            )
        ).toObjects(Personnel::class.java)
}