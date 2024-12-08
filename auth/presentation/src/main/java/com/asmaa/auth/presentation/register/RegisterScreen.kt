package com.asmaa.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asmaa.auth.domain.UseDataValidator
import com.asmaa.auth.presentation.R
import com.asmaa.core.presentation.designsystem.CheckIcon
import com.asmaa.core.presentation.designsystem.CrossIcon
import com.asmaa.core.presentation.designsystem.EmailIcon
import com.asmaa.core.presentation.designsystem.GradientBackground
import com.asmaa.core.presentation.designsystem.Poppins
import com.asmaa.core.presentation.designsystem.RuntasticDarkRed
import com.asmaa.core.presentation.designsystem.RuntasticGray
import com.asmaa.core.presentation.designsystem.RuntasticGreen
import com.asmaa.core.presentation.designsystem.RuntasticTheme
import com.asmaa.core.presentation.designsystem.components.RuntasticActionButton
import com.asmaa.core.presentation.designsystem.components.RuntasticPasswordTextField
import com.asmaa.core.presentation.designsystem.components.RuntasticTextField
import com.asmaa.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable

fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()

) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is RegisterEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            RegisterEvent.RegistrationSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context, R.string.registeration_successful,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulRegistration()
            }
        }
    }
    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable

private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit

) {
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.create_account),
                style = MaterialTheme.typography.headlineMedium
            )
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    append(stringResource(id = R.string.already_have_an_account))
                    pushStringAnnotation(
                        tag = "clickable_text",
                        annotation = stringResource(R.string.login)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Poppins
                        )
                    ) {
                        append(stringResource(id = R.string.login))
                    }
                }
            }
            ClickableText(text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "clickable_text",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        onAction(RegisterAction.onLoginClick)
                    }
                }
            )
            Spacer(
                modifier = Modifier
                    .padding(48.dp)
            )
            RuntasticTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = if (state.isEmailValid) {
                    CheckIcon
                } else null,
                hint = stringResource(id = R.string.example_email),
                title = stringResource(id = R.string.email),
                modifier = Modifier.fillMaxWidth(),
                error = "",
                additionalInfo = stringResource(id = R.string.must_be_a_valid_email),
                keyboardType = KeyboardType.Email
            )
            Spacer(
                modifier = Modifier
                    .padding(16.dp)
            )
            RuntasticPasswordTextField(
                state = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = { onAction(RegisterAction.onPasswordVisibilityClick) },
                hint = stringResource(id = R.string.password),
                title = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier
                    .padding(16.dp)
            )

            PasswordRequirement(
                text = stringResource(
                    id = R.string.atleast_x_characters,
                    UseDataValidator.MIN_PASSWORD_LENGTH
                ), isValid = state.passwordValidState.hasMinLength
            )

            PasswordRequirement(
                text = stringResource(
                    id = R.string.atleast_one_number,
                    UseDataValidator.MIN_PASSWORD_LENGTH
                ), isValid = state.passwordValidState.hasNumber
            )

            PasswordRequirement(
                text = stringResource(
                    id = R.string.contains_uppercase_char,
                    UseDataValidator.MIN_PASSWORD_LENGTH
                ), isValid = state.passwordValidState.hasUpperCaseChar
            )

            PasswordRequirement(
                text = stringResource(
                    id = R.string.contains_lowercase_char,
                    UseDataValidator.MIN_PASSWORD_LENGTH
                ), isValid = state.passwordValidState.hasLowerCaseChar
            )

            Spacer(
                modifier = Modifier
                    .padding(32.dp)
            )
            RuntasticActionButton(
                text = stringResource(id = R.string.register),
                isLoading = state.isRegistering,
                enabled = state.canRegister,
                modifier = Modifier.fillMaxWidth(),
            ) {
                onAction(RegisterAction.onRegisterClick)
            }
        }
    }
}

@Composable
private fun PasswordRequirement(text: String, isValid: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isValid) CheckIcon else CrossIcon,
            contentDescription = null,
            tint = if (isValid) RuntasticGreen else RuntasticDarkRed
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RuntasticTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}