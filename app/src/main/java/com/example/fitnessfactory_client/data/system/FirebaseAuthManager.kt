package com.example.fitnessfactory_client.data.system

import android.content.Intent
import android.util.Log
import com.example.fitnessfactory_client.FFApp
import com.example.fitnessfactory_client.data.ObfuscateData
import com.example.fitnessfactory_client.data.models.AppUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager(private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()) {

    suspend fun handleSignIn(googleSignInAccount: GoogleSignInAccount): AppUser {
        val authCredentials = getCredentials(googleSignInAccount)
        mAuth.signInWithCredential(authCredentials).await()
        val currentUser = mAuth.currentUser
        val appUser = AppUser()
        currentUser?.email?.let {
            appUser.email = it
        }
        currentUser?.displayName?.let {
            appUser.name = it
        }

        return appUser
    }

    private fun getCredentials(signInAccount: GoogleSignInAccount): AuthCredential =
        GoogleAuthProvider.getCredential(signInAccount.idToken, null)


    fun authStatusListenerFLow(): Flow<Boolean> =
        callbackFlow {
            Log.d("TAG", "started flow")
            val authListener = FirebaseAuth.AuthStateListener { authentication ->
                Log.d("TAG", "fired callback")
                val user = authentication.currentUser
                val isLoggedIn = user != null

                this.trySend(isLoggedIn)
            }
            Log.d("TAG", "started listening")
            mAuth.addAuthStateListener(authListener)

            awaitClose {
                Log.d("TAG", "finished listening")
                mAuth.removeAuthStateListener(authListener)
            }
        }

    fun isLoggedInFlow(): Flow<Boolean> =
        flow {
            emit(isLoggedIn())
        }

    private fun isLoggedIn(): Boolean =
        mAuth.currentUser != null

    fun signOutFlow(): Flow<Boolean> =
        flow {
            emit(signOut())
        }

    private suspend fun signOut(): Boolean {
        mAuth.signOut()
        val isFirebaseSignedOut = mAuth.currentUser == null
        val isGoogleSignedOut = signOutGoogle()

        return isFirebaseSignedOut && isGoogleSignedOut
    }

    private suspend fun signOutGoogle(): Boolean {
        getGoogleSignInClient().signOut().await()

        return true
    }

    private fun getGoogleSignInClient(): GoogleSignInClient =
        GoogleSignIn.getClient(FFApp.instance, getSignInOptions())

    private fun getSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ObfuscateData.getWebClientId())
            .requestEmail()
            .build()
    }
}