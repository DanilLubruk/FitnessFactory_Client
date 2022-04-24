package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.utils.StringUtils
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class UsersRepository : BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getUsersCollection()

    suspend fun getAppUserById(userId: String): AppUser =
        (getCollection().document(userId).get().await()).toObject(AppUser::class.java) ?: AppUser()

    suspend fun getAppUserByEmail(usersEmail: String): AppUser {
        val usersList = getQuerySnapshot(
            getCollection().whereEqualTo(
                AppUser.EMAIL_FIELD,
                usersEmail
            )
        ).documents

        if (!isEntityUnique(usersList)) {
            throw Exception(StringUtils.getMessageErrorUsersSameEmail())
        }

        return usersList.get(0).toObject(AppUser::class.java) as AppUser
    }


    suspend fun getAppUsersByPersonnel(personnelList: List<Personnel>): List<AppUser> {
        val personnelIds = ArrayList<String>()

        personnelList.forEach { personnel ->
            personnelIds.add(personnel.userId)
        }

        return getAppUsersByIds(usersIds = personnelIds)
    }

    suspend fun getAppUsersByIds(usersIds: List<String>): List<AppUser> {
        if (usersIds.isEmpty()) {
            return ArrayList()
        }

        val appUsers: ArrayList<AppUser> = ArrayList()
        getCollection().get().await().toObjects(AppUser::class.java).forEach {
            if (usersIds.contains(it.id)) {
                appUsers.add(it)
            }
        }

        appUsers.sortBy { it.email }

        return appUsers
    }


    suspend fun registerUserAsync(appUser: AppUser) {
        val isRegistered = checkUserRegistered(usersEmail = appUser.email)
        if (!isRegistered) {
            registerUser(appUser = appUser)
        }
    }

    private suspend fun checkUserRegistered(usersEmail: String): Boolean {
        val usersWithEmailAmount =
            getEntitiesAmount(QueryBuilder().whereEmailEquals(usersEmail).build())

        return usersWithEmailAmount > 0
    }

    private suspend fun registerUser(appUser: AppUser) {
        val documentReference = getCollection().document();
        appUser.id = documentReference.id

        documentReference.set(appUser).await()
    }

    private inner class QueryBuilder {

        private var query: Query = getCollection()

        fun whereEmailEquals(email: String): QueryBuilder {
            query = query.whereEqualTo(AppUser.EMAIL_FIELD, email)
            return this
        }

        fun build(): Query =
            query
    }
}