package com.example.fitnessfactory_client.data.firestoreCollections

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

abstract class CollectionOperator {

    private lateinit var collectionReference: CollectionReference

    protected fun getCollection(): CollectionReference {
        initCollection()
        return collectionReference
    }

    private fun initCollection() {
        collectionReference = FirebaseFirestore.getInstance().collection(getRoot())
    }

    protected abstract fun getRoot(): String
}