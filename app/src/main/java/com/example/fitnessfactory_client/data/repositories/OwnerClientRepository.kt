package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.utils.StringUtils

class OwnerClientRepository: OwnerPersonnelRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerClientsCollection()

    override fun getNotUniqueEmailMessage(): String =
        StringUtils.getMessageErrorClientsSameEmail()
}