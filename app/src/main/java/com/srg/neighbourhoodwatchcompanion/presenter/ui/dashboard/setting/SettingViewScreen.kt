package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting

import android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.presenter.theme.colors
import com.srg.neighbourhoodwatchcompanion.presenter.theme.typo
import com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home.HomeState


@OptIn(ExperimentalMaterial3Api::class)
@BottomNavGraph
@Composable
fun SettingsViewScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    appNavigator: AppNavigator
) {
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(uiState) {
        when (uiState) {
            is BaseViewState.Data -> {
                val data = uiState.cast<BaseViewState.Data<SettingState>>().value
                if (data.navigateToLoginScreen == true) {
                    appNavigator.openLoginScreen()
                    viewModel.clearState()
                }
            }

            else -> {}
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            SettingsSection("User Settings") {
                SettingsNavItem(icon = Icons.Default.Person, title = "Account") {
                    appNavigator.openAccountEditScreen()
                }
            }

            SettingsSection("Notifications") {
                SettingsToggleItem(icon = Icons.Default.Notifications, title = "Push Notifications")
                SettingScreenHorizontalDivider()
                SettingsToggleItem(icon = Icons.Default.Email, title = "Email Notifications")
            }

            SettingsSection("Privacy") {
                SettingsNavItem(
                    icon = Icons.Default.LocationOn, title = "Location Sharing"
                ) {
                    appNavigator.openPrivacyPolicyScreen()
                }
            }

            SettingsSection("About") {
                SettingsNavItem(icon = Icons.Default.Build, title = "Contact Us") {
                    appNavigator.openContactUsScreen()
                }
            }
            SettingsSection("Logout") {
                SettingsNavItem(icon = Icons.AutoMirrored.Filled.ExitToApp, title = "Logout") {
                    viewModel.onTriggerEvent(SettingEvent.SignOutEvent)
                }
            }
        }
    }

}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = typo.titleMedium,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = colors.background,
            shadowElevation = 1.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsNavItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon, contentDescription = title,
            modifier = Modifier.size(28.dp),
            tint = colors.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title, style = typo.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = "Go",
            modifier = Modifier.size(28.dp),
            tint = colors.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsToggleItem(icon: ImageVector, title: String) {
    var checked by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon, contentDescription = title,
            modifier = Modifier.size(28.dp),
            tint = colors.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = typo.bodyLarge, modifier = Modifier.weight(1f))
        Switch(
            modifier = Modifier.size(28.dp),
            checked = checked,
            onCheckedChange = { checked = it },
        )
    }
}

@Composable
fun SettingScreenHorizontalDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp),
        color = LightGray.copy(alpha = 0.4f)
    )

}

