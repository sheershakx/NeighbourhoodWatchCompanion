package com.srg.neighbourhoodwatchcompanion.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srg.neighbourhoodwatchcompanion.presenter.theme.Black


@Composable
fun InputValidationTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    inputWrapper: Pair<String, String>,
    placeHolder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    mTrailingIcon: @Composable (() -> Unit)? = null,
    mReadOnly: Boolean = false,
    mInteractionSource: MutableInteractionSource? = null,
    onValueChange: (String) -> Unit
) {
    val hasError = inputWrapper.second.isNotEmpty()
    Column(modifier = Modifier.height(IntrinsicSize.Min)) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            maxLines = 1,
            value = inputWrapper.first,
            isError = hasError,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            trailingIcon = mTrailingIcon,
            readOnly = mReadOnly,
            interactionSource = mInteractionSource,
            placeholder = { Text(placeHolder) }
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

@Composable
fun MyLabel(label: String) {
    Text(
        modifier = Modifier.padding(start = 10.dp),
        text = label,
        style = TextStyle.Default.copy(
            fontWeight = FontWeight.Thin,
            color = Black,
            fontFamily = FontFamily.Serif
        )
    )
}

@Composable
fun MyTextField(stringValue: String, enabled: Boolean = true, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        enabled = enabled,
        value = stringValue,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(15.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewInputValidationTextField() {
    InputValidationTextField(
        Modifier, Pair("", ""), "testlabel"
    ) { }
}

