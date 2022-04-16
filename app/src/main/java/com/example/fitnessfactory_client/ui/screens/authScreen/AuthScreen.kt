package com.example.fitnessfactory_client.ui.screens.authScreen

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.AppPrefs
import com.example.fitnessfactory_client.data.beans.OwnersData
import com.example.fitnessfactory_client.utils.DialogUtils
import com.example.fitnessfactory_client.utils.GuiUtils
import com.example.fitnessfactory_client.utils.ResUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.ExperimentalCoroutinesApi

object AuthScreen {

    private val BUNDLE_EXTRA = "BUNDLE_EXTRA"

    private val signInFailed: String = ResUtils.getString(R.string.message_error_sign_in_failed)
    private val signInCaption: String = ResUtils.getString(R.string.caption_sing_in_button)
    private val signInProcessCaption: String = ResUtils.getString(R.string.caption_sign_in_process)
    private val ownerPickerDialogTitle: String = ResUtils.getString(R.string.title_owner_picker)
    private const val signInButtonDescription: String = "SignInButton"

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun AuthScreen(
        lifecycle: Lifecycle,
        openHomeScreen: () -> Unit
    ) {
        val activity = (LocalContext.current as? Activity)
        BackHandler {
            activity?.finish();
        }
        val viewModel: AuthScreenViewModel = viewModel(factory = AuthScreenViewModelFactory())
        var text by rememberSaveable { mutableStateOf("") }
        var showDialog by rememberSaveable { mutableStateOf(false) }
        var isLoading by rememberSaveable { mutableStateOf(false) }
        val savedState by rememberSaveable { mutableStateOf(Bundle()) }
        val signInRequestCode = 1

        var ownersData by remember { mutableStateOf(OwnersData()) }
        LaunchedEffect(key1 = Unit) {
            ownersData.restoreState(savedState = savedState)
        }
        LaunchedEffect(ownersData) {
            ownersData.saveState(savedState = savedState)
        }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getRegisterUiState().collect { uiState ->
                    when (uiState) {
                        is RegisterUiState.Success ->
                            viewModel.getOwnersData(uiState.usersEmail)
                        is RegisterUiState.Error -> {
                            text = signInFailed
                            isLoading = false
                            viewModel.signOut()
                        }
                    }
                }
            }
        }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getOwnersData().collect { uiState ->
                    when (uiState) {
                        is PickOwnerUiState.Success -> {
                            ownersData = uiState.ownersData
                            showDialog = true
                        }
                        is PickOwnerUiState.Error -> {
                            text = signInFailed
                            isLoading = false
                            viewModel.signOut()
                        }
                    }
                }
            }
        }

        val authResultLauncher =
            rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
                try {
                    val account = task?.getResult(ApiException::class.java)
                    if (account == null) {
                        text = signInFailed
                        isLoading = false
                    } else {
                        viewModel.registerUser(googleSignInAccount = account)
                    }
                } catch (e: ApiException) {
                    text = signInFailed
                    isLoading = false
                    viewModel.signOut()
                    GuiUtils.showMessage(e.localizedMessage)
                }
            }

        AuthView(
            isLoading = isLoading,
            errorText = text,
            onClick = {
                isLoading = true
                text = ""
                authResultLauncher.launch(signInRequestCode)
            })

        if (showDialog) {
            DialogUtils.SingleGymOwnerSelectDialog(
                title = ownerPickerDialogTitle,
                ownersData = ownersData,
                onSubmitButtonClick = { owner ->
                    run {
                        AppPrefs.gymOwnerId().value = owner.id
                        viewModel.registerClient(ownerId = owner.id)
                        showDialog = false
                        isLoading = false
                        openHomeScreen()
                    }
                },
                onDismissRequest = {
                    showDialog = false
                    isLoading = false
                    text = signInFailed
                    viewModel.signOut()
                }
            )
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun AuthView(
        isLoading: Boolean = false,
        errorText: String?,
        onClick: () -> Unit
    ) {
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
                        onClick()
                    }
                )

                errorText?.let { errorMessage ->
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
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = signInButtonDescription,
                    tint = Color.Unspecified
                )
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