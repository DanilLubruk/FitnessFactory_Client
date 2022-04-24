package com.example.fitnessfactory_client.data.managers

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class UsersAccessManager {

    private fun getAdminsAccessCollection(): CollectionReference =
        FirebaseFirestore.getInstance().collection(FirestoreCollections.appDataCollectionValue)
}