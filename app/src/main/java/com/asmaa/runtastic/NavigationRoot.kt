package com.asmaa.runtastic

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.asmaa.auth.presentation.intro.IntroScreenRoot
import com.asmaa.auth.presentation.login.LoginScreenRoot
import com.asmaa.auth.presentation.register.RegisterScreenRoot
import com.asmaa.run.presentation.active_run.ActiveRunScreenRoot
import com.asmaa.run.presentation.active_run.ActiveRunService
import com.asmaa.run.presentation.run_overview.RunOverViewScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController, isLoggedIn: Boolean) {
    NavHost(navController, startDestination = if (isLoggedIn) "run" else "auth") {
        AuthGraph(navController)
        runGraph(navController)
    }
}

private fun NavGraphBuilder.AuthGraph(navController: NavHostController) {
    navigation(
        startDestination = "intro",
        route = "auth"
    ) {
        composable(route = "intro")
        {
            IntroScreenRoot(
                onSignupClick = { navController.navigate("register") },
                onSignInClick = { navController.navigate("login") }
            )
        }

        composable(route = "register")
        {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = { navController.navigate("login") }
            )
        }

        composable(route = "login") {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate("run") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate("register") {
                        popUpTo("login") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}


private fun NavGraphBuilder.runGraph(navController: NavHostController) {
    navigation(startDestination = "run_overview", route = "run") {
        composable("run_overview") {
            RunOverViewScreenRoot(onStartRunClick = {
                navController.navigate("active_run")
            })
        }
        composable("active_run", deepLinks = listOf(
            navDeepLink {
                uriPattern = "runtastic://active_run"
            }
        )) {
            val context = LocalContext.current
            ActiveRunScreenRoot(
                onBack = {
                    navController.navigateUp()
                },
                onFinish = {
                    navController.navigateUp()
                },
                onServiceToggle = { shouldServiceRun ->
                    if (shouldServiceRun) {
                        context.startService(
                            ActiveRunService.createStartIntent(
                                context, MainActivity::class.java
                            )
                        )
                    } else {
                        context.startService(
                            ActiveRunService.createStopIntent(
                                context
                            )
                        )
                    }
                }
            )
        }
    }

}