@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")

package com.asmaa.auth.presentation.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asmaa.auth.domain.AuthRepository
import com.asmaa.auth.domain.UseDataValidator
import com.asmaa.auth.presentation.R
import com.asmaa.core.domain.util.DataError
import com.asmaa.core.domain.util.Result
import com.asmaa.core.presentation.ui.UiText
import com.asmaa.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UseDataValidator
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        combine(state.email.textAsFlow(), state.password.textAsFlow())
        { email, password ->
            state =
                state.copy(
                    canLogin = userDataValidator.isValidEmail(
                        email = email.toString().trim()
                    ) && password.isNotEmpty()
                )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> {
                login()
            }

            LoginAction.OnTogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLogginIn = true)
            val result =
                authRepository.login(
                    state.email.text.toString().trim(),
                    state.password.text.toString()
                )
            state = state.copy(isLogginIn = true)
            when (result) {
                is Result.Error ->
                    if (result.error == DataError.NetworkError.UNAUTHORIZED) {
                        eventChannel.send(LoginEvent.Error(UiText.StringResource(R.string.error_email_password_incorrect)))
                    } else {
                        eventChannel.send(LoginEvent.Error(result.error.asUiText()))
                    }

                is Result.Success -> eventChannel.send(LoginEvent.LoginSuccess)
            }
        }
    }

}