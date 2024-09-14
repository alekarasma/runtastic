package com.asmaa.auth.domain

data class PasswordValidationState(
    val hasNumber: Boolean = false,
    val hasLowerCaseChar: Boolean = false,
    val hasMinLength: Boolean = false,
    val hasUpperCaseChar: Boolean = false
) {
    val isPasswordValid: Boolean
        get() = hasNumber && hasUpperCaseChar && hasLowerCaseChar && hasMinLength
}