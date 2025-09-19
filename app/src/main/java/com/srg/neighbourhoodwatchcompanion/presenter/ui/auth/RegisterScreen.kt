package com.srg.neighbourhoodwatchcompanion.presenter.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.common.InputValidationTextField
import com.srg.neighbourhoodwatchcompanion.common.MediumSpacer
import com.srg.neighbourhoodwatchcompanion.common.SmallSpacer
import com.srg.neighbourhoodwatchcompanion.common.StringResources
import com.srg.neighbourhoodwatchcompanion.common.showToast
import com.srg.neighbourhoodwatchcompanion.presenter.theme.colors
import com.srg.neighbourhoodwatchcompanion.presenter.theme.typo
import io.github.jan.supabase.exceptions.BadRequestRestException

@Destination
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val email by viewModel.emailValue.collectAsState()
    val password by viewModel.passwordValue.collectAsState()
    val confirmPassword by viewModel.confirmPasswordValue.collectAsState()
    val isFormValid by viewModel.isRegistrationFormValid().collectAsState()
    viewModel.setCurrentScreen(AuthScreen.REGISTER_SCREEN)

    LaunchedEffect(key1 = Unit) {
        viewModel.setCurrentScreen(AuthScreen.REGISTER_SCREEN)
    }

    LaunchedEffect(key1 = uiState) {
        when (uiState) {
            is BaseViewState.Data -> {
                val authState = uiState.cast<BaseViewState.Data<AuthState>>().value
                authState.userRegistrationState?.let {
                    context.showToast("User registered successfully! ${it.email}")
                }
            }

            is BaseViewState.Error -> {
                val error = uiState.cast<BaseViewState.Error>().throwable
                if (error is BadRequestRestException) {
                    context.showToast(
                        error.description
                            ?: error.error
                    )
                } else {
                    context.showToast(error.message.toString())
                }

            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.surface), // Light gray background
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register Screen",
            style = typo.titleLarge.copy(color = colors.onSurface),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Space between input fields
            ) {
                InputValidationTextField(
                    Modifier,
                    inputWrapper = email,
                    placeHolder = "Email",
                    onValueChange = viewModel::updateEmail
                )
                SmallSpacer()
                InputValidationTextField(
                    Modifier,
                    inputWrapper = password,
                    placeHolder = "Password",
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = viewModel::updatePassword
                )
                SmallSpacer()
                InputValidationTextField(
                    Modifier,
                    inputWrapper = confirmPassword,
                    placeHolder = "Confirm Password",
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = viewModel::updateConfirmPassword
                )

                MediumSpacer()

                Button(
                    onClick = {
                        viewModel.onTriggerEvent(AuthEvent.Register)
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary // Dark gray button color
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (uiState is BaseViewState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = StringResources.REGISTER,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}