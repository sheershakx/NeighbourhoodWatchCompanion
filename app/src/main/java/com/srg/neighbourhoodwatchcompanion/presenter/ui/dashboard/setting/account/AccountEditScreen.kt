package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.R
import com.srg.neighbourhoodwatchcompanion.common.MediumSpacer
import com.srg.neighbourhoodwatchcompanion.common.MyLabel
import com.srg.neighbourhoodwatchcompanion.common.MyTextField
import com.srg.neighbourhoodwatchcompanion.common.StringResources.EMAIL
import com.srg.neighbourhoodwatchcompanion.common.StringResources.FIRST_NAME
import com.srg.neighbourhoodwatchcompanion.common.StringResources.LAST_NAME
import com.srg.neighbourhoodwatchcompanion.common.StringResources.MOBILE
import com.srg.neighbourhoodwatchcompanion.common.showToast
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Pink40


@BottomNavGraph
@Composable
fun AccountEditScreen(
    viewModel: AccountEditViewModel = hiltViewModel(),
    appNavigator: AppNavigator,
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val mobile by viewModel.mobile.collectAsState()
    val areInputsValid by viewModel.areInputsValid().collectAsState()


    LaunchedEffect(uiState) {
        when (uiState) {
            is BaseViewState.Data -> {
                val accountEditState = uiState.cast<BaseViewState.Data<AccountEditState>>().value
                if (accountEditState.updateSuccessful == true) {
                    context.showToast("Profile updated successfully")
                    appNavigator.navigateBack()
                    viewModel.clearState()
                }
            }

            is BaseViewState.Error -> {}

            else -> {}
        }

    }



    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .background(color = Pink40)
            )
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    MyLabel(FIRST_NAME)
                    MyTextField(firstName) {
                        viewModel.updateFirstName(it)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    MyLabel(LAST_NAME)
                    MyTextField(lastName) {
                        viewModel.updateLastName(it)
                    }
                }
            }

            MediumSpacer()
            MyLabel(EMAIL)
            MyTextField(email, false) {
                viewModel.updateEmail(it)
            }
            MediumSpacer()
            MyLabel(MOBILE)
            MyTextField(mobile) {
                viewModel.updateMobile(it)
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 15.dp),
            enabled = areInputsValid,
            onClick = {
                viewModel.onTriggerEvent(AccountEditEvents.SubmitChanges)
                //verify textbox and update to db
            }
        ) {
            Text("Save changes")
        }
    }
}

