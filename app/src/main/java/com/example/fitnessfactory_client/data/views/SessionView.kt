package com.example.fitnessfactory_client.data.views

import com.example.fitnessfactory_client.data.models.Session

class SessionView(var session: Session) {

    lateinit var sessionTypeName: String
    lateinit var gymName: String
}