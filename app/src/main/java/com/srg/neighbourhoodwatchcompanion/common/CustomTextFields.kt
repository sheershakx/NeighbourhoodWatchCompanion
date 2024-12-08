package com.srg.neighbourhoodwatchcompanion.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun InputValidationTextField(
    modifier: Modifier,
    inputWrapper: Pair<String, String>,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit
) {
    val hasError = inputWrapper.second.isNotEmpty()
    Column {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            maxLines = 1,
            value = inputWrapper.first,
            isError = hasError,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            label = { Text(label) }
        )

        if (hasError) {
            Text(
                text = inputWrapper.second,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }


    }

}