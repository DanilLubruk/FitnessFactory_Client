package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils

class OwnerCoachRepository: OwnerPersonnelRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerCoachesCollection()

    override fun getNotUniqueEmailMessage(): String =
        StringUtils.getMessageErrorCoachesSameEmail()
}