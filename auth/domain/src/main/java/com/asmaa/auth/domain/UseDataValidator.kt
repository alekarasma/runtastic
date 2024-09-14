package com.asmaa.auth.domain

class UseDataValidator(private val patternValidator: PatternValidator) {
    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasLowerCaseCharacter = password.any { it.isLowerCase() }
        val hasUpperCaseCharacter = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasNumber = hasDigit,
            hasLowerCaseChar = hasLowerCaseCharacter,
            hasMinLength = hasMinLength,
            hasUpperCaseChar = hasUpperCaseCharacter
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 9

    }
}