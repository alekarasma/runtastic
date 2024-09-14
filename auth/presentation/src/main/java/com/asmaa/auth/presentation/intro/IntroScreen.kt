package com.asmaa.auth.presentation.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asmaa.auth.presentation.R
import com.asmaa.core.presentation.designsystem.GradientBackground
import com.asmaa.core.presentation.designsystem.LogoIcon
import com.asmaa.core.presentation.designsystem.RuntasticTheme
import com.asmaa.core.presentation.designsystem.components.RuntasticActionButton
import com.asmaa.core.presentation.designsystem.components.RuntasticOutlinedActionButton

@Composable
fun IntroScreenRoot(onSignupClick: () -> Unit, onSignInClick: () -> Unit) {
    IntroScreen(
        onAction = { action ->
            when (action) {
                IntroAction.OnSignInClick -> onSignInClick()
                IntroAction.OnSignupClick -> onSignupClick()
            }
        })
}

@Composable
fun IntroScreen(onAction: (IntroAction) -> Unit) {
    GradientBackground {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        )
        {
            LogoVertical()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.welcome),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.runtastic_description),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(32.dp))
            RuntasticOutlinedActionButton(
                text = stringResource(id = R.string.sign_in),
                false,
                modifier = Modifier.fillMaxWidth(),
                true
            ) {
                onAction(IntroAction.OnSignInClick)
            }
            Spacer(modifier = Modifier.height(8.dp))
            RuntasticActionButton(modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.sign_up),
                isLoading = false,
                enabled = true,
                onClick = {
                    onAction(IntroAction.OnSignupClick)

                })
        }
    }
}

@Composable
private fun LogoVertical(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = LogoIcon,
            contentDescription = "Brand Logo",
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.runtastic),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

}

@Preview
@Composable
private fun IntroScreenPreview() {
    RuntasticTheme {
        IntroScreen(
            onAction = {}
        )
    }
}