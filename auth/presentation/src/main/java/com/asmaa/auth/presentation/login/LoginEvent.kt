package com.asmaa.auth.presentation.login

import com.asmaa.core.presentation.ui.UiText

interface LoginEvent {
    data class Error(val error: UiText) : LoginEvent
    data object LoginSuccess : LoginEvent
}