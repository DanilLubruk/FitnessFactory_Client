package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.tasks.await
import java.lang.Exception

abstract class OwnerPersonnelRepository : BaseRepository() {

    protected abstract fun getNotUniqueEmailMessage(): String

    suspend fun getPersonnelList(): List<Personnel> =
        getQuerySnapshot(getCollection().orderBy(Personnel.USER_ID_FIELD)).toObjects(Personnel::class.java)

    suspend fun getPersonnelById(userId: String): Personnel =
        getCollection().document(userId).get().await().toObject(Personnel::class.java)
            ?: throw Exception(StringUtils.getMessageErrorUsersSameEmail())
}
