package com.example.fitnessfactory_client.ui.activities.authActivity

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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.repeatOnLifecycle
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.utils.DialogUtils
import com.example.fitnessfactory_client.utils.GuiUtils
import com.example.fitnessfactory_client.utils.ResUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AuthActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthActivityViewModel
    private val signInFailed: String = ResUtils.getString(R.string.message_error_sign_in_failed)
    private val signInCaption: String = ResUtils.getString(R.string.caption_sing_in_button)
    private val signInProcessCaption: String = ResUtils.getString(R.string.caption_sign_in_process)
    private val ownerPickerDialogTitle: String = ResUtils.getString(R.string.title_owner_picker)
    private val signInButtonDescription: String = "SignInButton"

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
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

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun AuthScreen(viewModel: AuthActivityViewModel) {
        var text by remember { mutableStateOf("")}
        var showDialog by remember { mutableStateOf(false)}
        val signInRequestCode = 1

        LaunchedEffect(key1 = Unit) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerUiState.collect { uiState ->
                    when (uiState) {
                        is RegisterUiState.Success ->
                            showDialog = true
                        is RegisterUiState.Error -> {
                            text = signInFailed
                            GuiUtils.showMessage(uiState.exception.localizedMessage)
                        }
                        is RegisterUiState.Loading -> {}
                    }
                }
            }
        }

        val authResultLauncher = rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            try {
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    text = signInFailed
                } else {
                    account.displayName?.let { name ->
                        account.email?.let {
                                email -> viewModel.registerUser(usersName = name, usersEmail = email)
                        }
                    }
                }
            } catch (e: ApiException) {
                text = signInFailed
                GuiUtils.showMessage(e.localizedMessage)
            }
        }

        AuthView(
            errorText = text,
            onClick = {
                text = ""
                authResultLauncher.launch(signInRequestCode)
            })

        if (showDialog) {
            DialogUtils.SingleSelectDialog(
                title = ownerPickerDialogTitle,
                optionsList = listOf("Owner1", "Owner2", "Owner3"),
                onSubmitButtonClick = {item ->
                    run {
                        GuiUtils.showMessage(item)
                        showDialog = false
                    }
                },
                onDismissRequest = {showDialog = false}
            )
        }
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
                    captionText = signInCaption,
                    loadingText = signInProcessCaption,
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
        captionText: String,
        loadingText: String = signInProcessCaption,
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
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Icon(painter = icon, contentDescription = signInButtonDescription, tint = Color.Unspecified)
                Spacer(modifier = Modifier.width(8.dp))

                Text(text = if (isLoading) loadingText else captionText)
                if (isLoading) {
                    Spacer(modifier = Modifier.width(16.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp),
                        strokeWidth = 2.dp,
                        color = progressIndicatorColor
                    )
                }
            }
        }
    }
}