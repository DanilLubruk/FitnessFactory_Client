package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Personnel
import java.lang.Exception

abstract class OwnerPersonnelRepository: BaseRepository() {

    protected abstract fun getNotUniqueEmailMessage(): String

    suspend fun getPersonnelIdByEmail(personnelEmail: String): String {
        val documents =
            getQuerySnapshot(
            getCollection()
                .whereEqualTo(Personnel.USER_EMAIL_FIELD, personnelEmail))
                .documents
        if (!isEntityUnique(documents)) {
            throw Exception(getNotUniqueEmailMessage())
        }

        return documents.get(0).getString(Personnel.ID_FIELD) as String
    }
}
