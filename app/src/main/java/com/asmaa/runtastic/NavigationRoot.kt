package com.asmaa.runtastic

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.asmaa.auth.presentation.intro.IntroScreenRoot
import com.asmaa.auth.presentation.login.LoginScreenRoot
import com.asmaa.auth.presentation.register.RegisterScreenRoot

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

        }
    }

}