package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting.contact

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.LargeSpacer
import com.srg.neighbourhoodwatchcompanion.common.StringResources.CONTACT_EMAIL
import com.srg.neighbourhoodwatchcompanion.common.StringResources.CONTACT_US_MESSAGE
import com.srg.neighbourhoodwatchcompanion.common.StringResources.CONTACT_US_TITLE

@BottomNavGraph
@Composable
fun ContactUsScreen(
    appNavigator: AppNavigator
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = CONTACT_US_TITLE,
                style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LargeSpacer()
            Surface(
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = CONTACT_US_MESSAGE,
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Justify
                    )
                    LargeSpacer()
                    Text(
                        text = "📧 Email us at: $CONTACT_EMAIL",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$CONTACT_EMAIL")
                                putExtra(Intent.EXTRA_SUBJECT, "Email Developer")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }

        }
    }
}