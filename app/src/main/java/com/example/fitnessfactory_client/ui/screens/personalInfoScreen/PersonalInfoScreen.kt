package com.example.fitnessfactory_client.ui.screens.personalInfoScreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.TopBarAction
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.ui.components.TopBar
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.utils.DialogUtils
import kotlinx.coroutines.launch

object PersonalInfoScreen {

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    @Composable
    fun PersonalInfoScreen(navigateBack: () -> Unit) {
        val scope = rememberCoroutineScope()

        var id by rememberSaveable { mutableStateOf("") }
        var name by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }

        val lifecycleOwner = LocalLifecycleOwner.current
        val viewModel: PersonalInfoScreenViewModel =
            viewModel(factory = PersonalInfoScreenViewModelFactory())

        var showAskToCloseDialog by remember { mutableStateOf(false) }
        BackHandler {
            viewModel.fetchDbUser(id)
        }

        LaunchedEffect(key1 = Unit) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dbUserSharedFlow.collect(collector = { dbUser ->
                    val appUser = AppUser.newValue(id = id, name = name, email = email)
                    val isModified = !appUser.equals(dbUser)
                    if (isModified) {
                        showAskToCloseDialog = true
                    } else {
                        navigateBack()
                    }
                })
            }
        }

        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(key1 = Unit) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfoSharedFlow.collect(collector = { userInfo ->
                    id = userInfo.id
                    name = userInfo.name
                    email = userInfo.email
                    isLoading = false
                })
            }
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.fetchUserInfo()
        }

        Column(modifier = Modifier.fillMaxSize()) {

            AnimatedVisibility(
                visible = showAskToCloseDialog,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                DialogUtils.YesNoDialog(
                    onOkPress = {
                        showAskToCloseDialog = false
                        navigateBack()
                    },
                    onDismissRequest = { showAskToCloseDialog = false },
                    questionText = stringResource(id = R.string.caption_close_and_discard)
                )
            }

            TopBar.TopBar(
                title = DrawerScreens.PersonalInfo.title,
                buttonIcon = Icons.Filled.ArrowBack,
                onButtonClicked = {
                    navigateBack()
                },
                actions = listOf(
                    TopBarAction(
                        stringResource(id = R.string.caption_save),
                        image = Icons.Filled.Save,
                        imageTint = Color.White
                    ) {
                    }
                )
            )

            val keyboardController = LocalSoftwareKeyboardController.current

            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp),
                        strokeWidth = 2.dp,
                        color = colorResource(id = R.color.royalBlue)
                    )
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = name,
                        maxLines = 1,
                        onValueChange = {
                            name = it
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text(stringResource(id = R.string.caption_name)) },
                        textStyle = MaterialTheme.typography.body1,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = email,
                        maxLines = 1,
                        onValueChange = {
                            email = it
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        label = { Text(stringResource(id = R.string.caption_email)) },
                        textStyle = MaterialTheme.typography.body1,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White
                        )
                    )
                }
            }
        }
    }
}