package com.example.fitnessfactory_client.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfactory_client.R
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthActivityViewModel

    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            AuthActivityViewModelFactory()
        ).get(AuthActivityViewModel::class.java)

        setContent {
            AuthScreen(viewModel)
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun AuthScreen(viewModel: AuthActivityViewModel) {
        val coroutineScope = rememberCoroutineScope()
        var text by remember { mutableStateOf<String?>(null)}
        val signInRequestCode = 1

        val authResultLauncher = rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            text = try {
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    "Google sign in failed"
                } else {
                    "Sign in successfully done. Account ${account.displayName}";
                }
            } catch (e: ApiException) {
                "Google sign in failed"
            }
        }

        AuthView(
            errorText = text,
            onClick = {
                text = null
                authResultLauncher.launch(signInRequestCode)
            })
    }

    @ExperimentalMaterialApi
    @Composable
    fun AuthView(
        errorText: String?,
        onClick: () -> Unit
    ) {
        var isLoading by remember { mutableStateOf(false)}

        Scaffold {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignInButton(
                    text = "Sign in with Google",
                    loadingText = "Singing in...",
                    isLoading = isLoading,
                    icon = painterResource(id = R.drawable.ic_google_logo),
                    onClick = {
                        isLoading = true
                        onClick()
                    }
                )

                errorText?.let { errorMessage ->
                    isLoading = false
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = errorMessage)
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun SignInButton(
        text: String,
        loadingText: String = "Signing in...",
        icon: Painter,
        isLoading: Boolean = false,
        borderColor: Color = Color.LightGray,
        backgroundColor: Color = MaterialTheme.colors.surface,
        progressIndicatorColor: Color = MaterialTheme.colors.primary,
        onClick: () -> Unit
    ) {
        Surface(
            modifier = Modifier.clickable(enabled = !isLoading, onClick = onClick),
            border = BorderStroke(width = 1.dp, color = borderColor),
            color = backgroundColor
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                    .animateContentSize(animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Icon(painter = icon, contentDescription = "SignInButton", tint = Color.Unspecified)
                Spacer(modifier = Modifier.width(8.dp))

                Text(text = if (isLoading) loadingText else text)
                if (isLoading) {
                    Spacer(modifier = Modifier.width(16.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.height(16.dp).width(16.dp),
                        strokeWidth = 2.dp,
                        color = progressIndicatorColor
                    )
                }
            }
        }
    }
}