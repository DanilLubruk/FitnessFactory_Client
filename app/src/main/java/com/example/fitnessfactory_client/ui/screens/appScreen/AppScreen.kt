package com.example.fitnessfactory_client.ui.screens.appScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.SessionsFilter
import com.example.fitnessfactory_client.ui.components.Drawer
import com.example.fitnessfactory_client.ui.screens.Screens
import com.example.fitnessfactory_client.ui.screens.authScreen.*
import com.example.fitnessfactory_client.ui.screens.coachesScreen.CoachesScreen
import com.example.fitnessfactory_client.ui.screens.gymsScreen.GymsScreen
import com.example.fitnessfactory_client.ui.screens.homeScreen.HomeScreen
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.MySessionsListScreen
import com.example.fitnessfactory_client.ui.screens.sessionTypesScreen.SessionTypesScreen
import com.example.fitnessfactory_client.ui.screens.splashScreen.SplashScreen
import com.example.fitnessfactory_client.utils.GuiUtils
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class AppScreen : AppCompatActivity() {

    @OptIn(
        androidx.compose.animation.ExperimentalAnimationApi::class,
        androidx.compose.material.ExperimentalMaterialApi::class,
        androidx.compose.foundation.ExperimentalFoundationApi::class,
        com.google.accompanist.pager.ExperimentalPagerApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberAnimatedNavController()

            Scaffold {
                NavigationComponent(navController = navController)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    @Composable
    fun NavigationComponent(navController: NavHostController) {
        val systemUiController = rememberSystemUiController()
        val statusBarColor = colorResource(id = R.color.royalBlue)
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = statusBarColor,
            )
        }
        val viewModel: AppScreenViewModel = viewModel(factory = AppScreenViewModelFactory())
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }
        val closeDrawer = {
            scope.launch {
                drawerState.close()
            }
        }
        val logout: () -> Unit = {
            navController.navigate(Screens.AUTH_SCREEN) {
                popUpTo(Screens.AUTH_SCREEN)
            }
            closeDrawer()
        }
        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logoutState.collect { logoutState ->
                    when (logoutState) {
                        is LogoutState.Success -> logout()
                        is LogoutState.Failure -> GuiUtils.showMessage(logoutState.message)
                    }
                }
            }
        }

        var sessionsFilter by remember { mutableStateOf(SessionsFilter.getNoFilterEntity()) }
        val navigateHome = {
            navController.navigate(Screens.HOME_SCREEN) {
                popUpTo(Screens.HOME_SCREEN)
            }
        }
        val springSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy)

        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                Drawer.Drawer(
                    onDestinationClicked = { route ->
                        closeDrawer()
                        navController.navigate(route) {
                            popUpTo(Screens.HOME_SCREEN)
                        }
                    },
                    logout = { viewModel.logout() }
                )
            }) {
            AnimatedNavHost(
                navController = navController,
                startDestination = Screens.SPLASH_SCREEN
            ) {
                composable(Screens.SPLASH_SCREEN) {
                    SplashScreen.SplashScreen(
                        lifecycle = lifecycle,
                        openAuthScreen = { navController.navigate(Screens.AUTH_SCREEN) },
                        openHomeScreen = { navigateHome() }
                    )
                }
                composable(Screens.AUTH_SCREEN) {
                    AuthScreen.AuthScreen(
                        lifecycle = lifecycle,
                        openHomeScreen = { navigateHome() }
                    )
                }
                composable(
                    Screens.HOME_SCREEN) {
                    HomeScreen.HomeScreen(
                        lifecycle = lifecycle,
                        sessionsFilter = sessionsFilter,
                        openDrawer = { openDrawer() },
                        clearFilter = { sessionsFilter = SessionsFilter.getNoFilterEntity() },
                        setFilter = { sessionsFilter = it },
                        logout = { viewModel.logout() }
                    )
                }
                composable(Screens.MY_SESSIONS_SCREEN) {
                    MySessionsListScreen.MySessionsListScreen(
                        lifecycle = lifecycle,
                        openDrawer = { openDrawer() },
                    )
                }
                composable(Screens.COACHES_SCREEN) {
                    CoachesScreen.CoachesScreen(
                        lifecycle = lifecycle,
                        openDrawer = { openDrawer() },
                        showSessionsAction = { coachData ->
                            sessionsFilter =
                                SessionsFilter
                                    .builder()
                                    .filterCoach(coachData = coachData)
                                    .build()
                            navigateHome()
                        },
                    )
                }
                composable(Screens.SESSION_TYPES_SCREEN) {
                    SessionTypesScreen.SessionTypesScreen(
                        lifecycle = lifecycle,
                        openDrawer = { openDrawer() },
                        showSessionsAction = { sessionType ->
                            sessionsFilter =
                                SessionsFilter
                                    .builder()
                                    .filterSessionType(sessionType = sessionType)
                                    .build()
                            navigateHome()
                        },
                    )
                }
                composable(Screens.GYMS_SCREEN) {
                    GymsScreen.GymsScreen(
                        lifecycle = lifecycle,
                        openDrawer = { openDrawer() },
                        showSessionsAction = { gym ->
                            sessionsFilter =
                                SessionsFilter
                                    .builder()
                                    .filterGym(gym = gym)
                                    .build()
                            navigateHome()
                        },
                    )
                }
            }
        }
    }
}