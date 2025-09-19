package com.srg.neighbourhoodwatchcompanion.presenter.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.common.InputValidationTextField
import com.srg.neighbourhoodwatchcompanion.common.LargeSpacer
import com.srg.neighbourhoodwatchcompanion.common.MediumSpacer
import com.srg.neighbourhoodwatchcompanion.common.SmallSpacer
import com.srg.neighbourhoodwatchcompanion.common.StringResources
import com.srg.neighbourhoodwatchcompanion.common.showToast
import com.srg.neighbourhoodwatchcompanion.presenter.theme.colors
import com.srg.neighbourhoodwatchcompanion.presenter.theme.typo
import io.github.jan.supabase.exceptions.BadRequestRestException

@RootNavGraph(start = true)
@Destination(start = true)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    appNavigator: AppNavigator
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val email by viewModel.emailValue.collectAsState()
    val password by viewModel.passwordValue.collectAsState()
    val isFormValid by viewModel.isLoginFormValid().collectAsState()


    //todo research on inside launchedeffect-> uistate->baseview.data because if we need to preserve other parameter
    //todo of authState data class and only change one parameter and set state than, all three paramenets if else will
    //todo be called which will display 3 toast even if it was not triggered.
    //todo and if we set new state with new data each time then previous other saved data would be lost
    //todo so find the best solution of updating UI on state changes while preserving other state parameter value as well.
    LaunchedEffect(key1 = Unit) {
        viewModel.setCurrentScreen(AuthScreen.LOGIN_SCREEN)
    }

    LaunchedEffect(key1 = uiState) {
        when (uiState) {
            is BaseViewState.Data -> {
                val authState = uiState.cast<BaseViewState.Data<AuthState>>().value
                if (authState.isUserLoggedIn) {
                    appNavigator.openDashboardScreen()
                    viewModel.clearState()
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
            text = "Login Screen",
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

                LargeSpacer()
                Button(
                    onClick = { viewModel.onTriggerEvent(AuthEvent.Login) },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .align(Alignment.CenterHorizontally),
                    enabled = isFormValid
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        if (uiState is BaseViewState.Loading) CircularProgressIndicator(color = Color.White)
                        else Text(text = StringResources.LOGIN)
                    }
                }
                OutlinedButton(
                    onClick = {
                        appNavigator.openRegisterScreen()
                    }, modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}