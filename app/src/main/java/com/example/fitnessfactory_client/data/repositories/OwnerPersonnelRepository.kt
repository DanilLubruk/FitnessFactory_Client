package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.utils.StringUtils
import java.lang.Exception

abstract class OwnerPersonnelRepository : BaseRepository() {

    protected abstract fun getNotUniqueEmailMessage(): String

    suspend fun getPersonnelList(): List<Personnel> =
        getQuerySnapshot(getCollection().orderBy(Personnel.USER_EMAIL_FIELD)).toObjects(Personnel::class.java)

    suspend fun getPersonnelByEmail(personnelEmail: String): Personnel {
        val personnelList = getQuerySnapshot(
            getCollection().whereEqualTo(
                Personnel.USER_EMAIL_FIELD,
                personnelEmail
            )
        ).documents

        if (!isEntityUnique(personnelList)) {
            throw Exception(StringUtils.getMessageErrorUsersSameEmail())
        }

        return personnelList.get(0).toObject(Personnel::class.java) as Personnel
    }

    suspend fun getPersonnelEmails(): List<String> {
        val personnelList = getQuerySnapshot(getCollection()).toObjects(Personnel::class.java)
        val personnelEmails = ArrayList<String>()

        personnelList.forEach {
            personnelEmails.add(it.userEmail)
        }

        return personnelEmails
    }

    suspend fun getPersonnelIdByEmail(personnelEmail: String): String {
        val documents =
            getQuerySnapshot(
                getCollection()
                    .whereEqualTo(Personnel.USER_EMAIL_FIELD, personnelEmail)
            )
                .documents
        if (!isEntityUnique(documents)) {
            throw Exception(getNotUniqueEmailMessage())
        }

        return documents.get(0).getString(Personnel.ID_FIELD) as String
    }

    suspend fun getPersonnelEmailsByIds(personnelIds: List<String>): List<String> {
        val personnelEmails = ArrayList<String>()

        if (personnelIds.isEmpty()) {
            return personnelEmails
        }

        val personnelList =
            getQuerySnapshot(
                getCollection().whereIn(Personnel.ID_FIELD, personnelIds)
            ).toObjects(Personnel::class.java)

        personnelList.forEach {
            personnelEmails.add(it.userEmail)
        }

        return personnelEmails
    }
}
