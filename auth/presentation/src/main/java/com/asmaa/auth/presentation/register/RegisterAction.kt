package com.asmaa.auth.presentation.register

/**
 * Action has all the Action defined that can be done the UI screen
 */
sealed interface RegisterAction {
    data object onPasswordVisibilityClick : RegisterAction
    data object onLoginClick : RegisterAction
    data object onRegisterClick : RegisterAction
}