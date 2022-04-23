package com.example.fitnessfactory_client.data.repositories

import androidx.compose.ui.res.stringResource
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldPath
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
        val personnelEmails = ArrayList<String>()

        personnelList.forEach { personnel ->
            personnelEmails.add(personnel.userEmail)
        }

        return getAppUsersByEmails(usersEmails = personnelEmails)
    }

    suspend fun getAppUsersByEmails(usersEmails: List<String>): List<AppUser> {
        if (usersEmails.isEmpty()) {
            return ArrayList()
        }

        val appUsers = getQuerySnapshot(
            getCollection()
                .whereIn(AppUser.EMAIL_FIELD, usersEmails)
        )
            .toObjects(AppUser::class.java)

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