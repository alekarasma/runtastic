package com.asmaa.auth.presentation.register

import com.asmaa.core.presentation.ui.UiText
import java.lang.Error

/**
 * Action has all the Action defined that can be done the UI screen
 */
sealed interface RegisterEvent {
    data object RegistrationSuccess : RegisterEvent
    data class Error(val error: UiText) : RegisterEvent
}