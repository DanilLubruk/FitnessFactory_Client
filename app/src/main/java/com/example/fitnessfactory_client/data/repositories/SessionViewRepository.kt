package com.example.fitnessfactory_client.data.repositories

import android.text.TextUtils
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.data.views.SessionView
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

class SessionViewRepository : BaseRepository() {

    private fun getSessionTypesCollection() =
        getFirestore().collection(FirestoreCollections.getSessionTypesCollection())

    private fun getGymsCollection() =
        getFirestore().collection(FirestoreCollections.getOwnerGymsCollection())

    suspend fun getSessionViewsList(sessionsList: List<Session>): List<SessionView> {
        val sessionViewsList: ArrayList<SessionView> = ArrayList()

        sessionsList.forEach { session ->
            sessionViewsList.add(getSessionView(session = session))
        }

        return sessionViewsList
    }

    private suspend fun getSessionView(session: Session): SessionView {
        val sessionView: SessionView = SessionView(session)

        sessionView.sessionTypeName = getSessionTypeName(session.sessionTypeId)
        sessionView.gymName = getGymName(session.gymId)

        return sessionView
    }

    private suspend fun getSessionTypeName(sessionTypeId: String): String {
        if (TextUtils.isEmpty(sessionTypeId)) {
            return ""
        }

        val documents =
            getQuerySnapshot(getSessionTypesCollection()
                .whereEqualTo(SessionType.ID_FIELD, sessionTypeId))
                .documents

        if (!isEntityUnique(documents = documents)) {
            return ""
        }

        return documents.get(0).getString(SessionType.NAME_FIELD) as String
    }

    private suspend fun getGymName(gymId: String): String {
        if (TextUtils.isEmpty(gymId)) {
            return ""
        }

        val documents =
            getQuerySnapshot(getGymsCollection()
                .whereEqualTo(Gym.ID_FIELD, gymId))
                .documents

        if (!isEntityUnique(documents = documents)) {
            return ""
        }

        return documents.get(0).getString(Gym.NAME_FIELD) as String
    }
}