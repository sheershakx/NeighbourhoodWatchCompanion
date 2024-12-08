package com.srg.neighbourhoodwatchcompanion.presenter.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.srg.framework.base.mvi.BaseViewState
import com.srg.neighbourhoodwatchcompanion.common.InputValidationTextField
import com.srg.neighbourhoodwatchcompanion.common.LargeSpacer
import com.srg.neighbourhoodwatchcompanion.common.SmallSpacer


@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val email by viewModel.emailValue.collectAsState()
    val password by viewModel.passwordValue.collectAsState()
    val confirmPassword by viewModel.confirmPasswordValue.collectAsState()
    val isFormValid by viewModel.isFormValid().collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)


        ) {
            InputValidationTextField(
                Modifier,
                inputWrapper = email,
                label = "Email",
                onValueChange = viewModel::updateEmail
            )

            SmallSpacer()
            InputValidationTextField(
                Modifier,
                inputWrapper = password,
                label = "Password",
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = viewModel::updatePassword

            )

            SmallSpacer()
            InputValidationTextField(
                Modifier,
                inputWrapper = confirmPassword,
                label = "Confirm Password",
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = viewModel::updateConfirmPassword
            )

            LargeSpacer()
            Button(
                onClick = {
                    viewModel.onTriggerEvent(AuthEvent.Login)
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    if (uiState is BaseViewState.Loading) CircularProgressIndicator(color = Color.White)
                    else Text(text = "Register")
                }
            }
        }
    }
}