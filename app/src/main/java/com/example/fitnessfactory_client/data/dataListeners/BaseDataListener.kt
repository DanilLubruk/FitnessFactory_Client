package com.example.fitnessfactory_client.data.dataListeners

import com.example.fitnessfactory_client.data.firestoreCollections.CollectionOperator
import com.google.firebase.firestore.ListenerRegistration

abstract class BaseDataListener: CollectionOperator() {

    protected lateinit var listenerRegistration: ListenerRegistration
}