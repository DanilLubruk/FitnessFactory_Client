package com.example.fitnessfactory_client.data.system

import android.content.Intent
import com.example.fitnessfactory_client.FFApp
import com.example.fitnessfactory_client.data.ObfuscateData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthManager(mAuth: FirebaseAuth = FirebaseAuth.getInstance()) {

    suspend fun getSignInIntent(): Intent =
        getGoogleSignInClient().signInIntent

    private fun getGoogleSignInClient() =
        GoogleSignIn.getClient(FFApp.instance, getSignInOptions())

    private fun getSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ObfuscateData.getWebClientId())
            .requestEmail()
            .build()
}