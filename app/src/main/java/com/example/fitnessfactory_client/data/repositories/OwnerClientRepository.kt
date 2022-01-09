package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.utils.ResUtils

class OwnerClientRepository: OwnerPersonnelRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerClientsCollection()

    override fun getNotUniqueEmailMessage(): String =
        ResUtils.getString(R.string.message_error_clients_same_email)
}