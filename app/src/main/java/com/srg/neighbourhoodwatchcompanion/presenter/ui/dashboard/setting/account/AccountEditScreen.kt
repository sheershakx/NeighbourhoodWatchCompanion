package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting.account

import android.net.Uri
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.LargeSpacer
import com.srg.neighbourhoodwatchcompanion.common.MediumSpacer
import com.srg.neighbourhoodwatchcompanion.common.MyLabel
import com.srg.neighbourhoodwatchcompanion.common.MyTextField
import com.srg.neighbourhoodwatchcompanion.common.PermissionHelper.handleImagePermissionAndLaunchPicker
import com.srg.neighbourhoodwatchcompanion.common.StringResources.EMAIL
import com.srg.neighbourhoodwatchcompanion.common.StringResources.FIRST_NAME
import com.srg.neighbourhoodwatchcompanion.common.StringResources.LAST_NAME
import com.srg.neighbourhoodwatchcompanion.common.StringResources.MOBILE
import com.srg.neighbourhoodwatchcompanion.common.showToast
import com.srg.neighbourhoodwatchcompanion.presenter.theme.PurpleGrey40


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@BottomNavGraph
@Composable
fun AccountEditScreen(
    viewModel: AccountEditViewModel = hiltViewModel(),
    appNavigator: AppNavigator,
) {

    val activity = LocalActivity.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val mobile by viewModel.mobile.collectAsState()
    val areInputsValid by viewModel.areInputsValid().collectAsState()
    val profileImageUri by viewModel.profileImageUri.collectAsState()
    val profileImageLocal by viewModel.profileImageLocal.collectAsState()
    var imagePermissionRequested by rememberSaveable { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { result ->
        if (result == null) {
            return@rememberLauncherForActivityResult
        }
        viewModel.onTriggerEvent(AccountEditEvents.PreviewProfileImage(result))

    }
    val imagePermissionState =
        rememberPermissionState(android.Manifest.permission.READ_MEDIA_IMAGES)

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

            is BaseViewState.Error -> {
                val error = uiState.cast<BaseViewState.Error>().throwable
                context.showToast(error.message.toString())
            }

            else -> {}
        }

    }



    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState is BaseViewState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(105.dp)
                        .background(color = PurpleGrey40)
                )
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(if (profileImageUri != Uri.EMPTY) profileImageUri else profileImageLocal)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 60.dp)
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)
                )
                IconButton(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 80.dp),
                    onClick = {
                        handleImagePermissionAndLaunchPicker(
                            imagePermissionState,
                            imagePermissionRequested,
                            { imagePermissionRequested = it },
                            activity,
                            imagePickerLauncher
                        )
                    }) {
                    Icon(Icons.Filled.Edit, "Edit image")
                }

            }
            LargeSpacer()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 30.dp)
                    .alpha(if (uiState is BaseViewState.Loading) 0.4f else 1f),

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
                }
            ) {
                Text("Save changes")
            }
        }
    }
}

