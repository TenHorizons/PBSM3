package com.example.pbsm3.ui.commonScreenComponents.currencytextfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable

//TODO deal with color issue, and how app exits when value is ""
fun CurrencyTextField(
    modifier: Modifier = Modifier,
    isPositiveValue: Boolean,
    background: Color = MaterialTheme.colorScheme.surface,
    value: String,
    onValueChange: (String) -> Unit,
    errorCondition: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current
) {
    val visualTransformation =
        CurrencyAmountInputVisualTransformation(isPositiveValue = isPositiveValue)
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        visualTransformation = visualTransformation,
        singleLine = true,
        enabled = true,
        interactionSource = interactionSource,
        keyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        textStyle = textStyle
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(0.dp), // this is how you can remove the padding
            colors = TextFieldDefaults.textFieldColors(
                containerColor = background,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red
            ),
            isError = errorCondition,
        )
    }
}

@Preview
@Composable
fun CurrencyTextFieldTransactionPreview() {
    CurrencyTextField(
        value = 100.toString(),
        onValueChange = { },
        isPositiveValue = true,
        textStyle = TextStyle.Default.copy(
            fontSize = 36.sp,
            textAlign = TextAlign.End
        ),
        modifier = Modifier.requiredWidth(200.dp))
}

@Preview(showBackground = true)
@Composable
fun CurrencyTextFieldBudgetPreview() {
    CurrencyTextField(
        value = 100.toString(),
        onValueChange = { },
        isPositiveValue = true,
        modifier = Modifier.requiredWidth(100.dp),
        textStyle = TextStyle.Default.copy(
            textAlign = TextAlign.Center
        )
    )
}