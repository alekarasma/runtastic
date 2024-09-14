package com.asmaa.auth.presentation.intro

sealed interface IntroAction {
    data object OnSignupClick : IntroAction
    data object OnSignInClick : IntroAction
}