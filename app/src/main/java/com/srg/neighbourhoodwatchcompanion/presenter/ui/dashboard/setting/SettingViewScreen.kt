package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph


@OptIn(ExperimentalMaterial3Api::class)
@BottomNavGraph
@Composable
fun SettingsViewScreen(
    appNavigator: AppNavigator
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
//            SettingScreenHorizontalDivider()
//            SettingsNavItem(icon = Icons.Default.Home, title = "Neighborhood") { /* TODO */ }
        }

        SettingsSection("Notifications") {
            SettingsToggleItem(icon = Icons.Default.Notifications, title = "Push Notifications")
            SettingScreenHorizontalDivider()
            SettingsToggleItem(icon = Icons.Default.Email, title = "Email Notifications")
        }

        SettingsSection("Privacy") {
            SettingsNavItem(
                icon = Icons.Default.LocationOn, title = "Location Sharing"
            ) { /* TODO */ }
        }

        SettingsSection("About") {
            SettingsNavItem(icon = Icons.Default.Build, title = "Help") { /* TODO */ }
            SettingsNavItem(icon = Icons.Default.Build, title = "Contact Us") { /* TODO */ }
        }
    }

}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
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
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon, contentDescription = title,
            modifier = Modifier.size(28.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title, fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = "Go",
            modifier = Modifier.size(28.dp),
            tint = Color.Gray
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
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 18.sp, modifier = Modifier.weight(1f))
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

