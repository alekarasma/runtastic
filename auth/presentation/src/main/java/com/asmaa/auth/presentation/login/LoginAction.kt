package com.asmaa.auth.presentation.login

interface LoginAction {
    data object OnTogglePasswordVisibility : LoginAction
    data object OnLoginClick : LoginAction
    data object SignUpClick : LoginAction
}