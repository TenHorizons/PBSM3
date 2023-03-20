package com.example.pbsm3.ui.commonScreenComponents.currencytextfield

import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyTextField(
    modifier: Modifier = Modifier,
    positiveValue: Boolean,
    background: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    value: String,
    onValueChange: (String) -> Unit,
    errorCondition: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    defaultMinWidth: Dp = 10.dp
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation =
        CurrencyAmountInputVisualTransformation(positiveValue = positiveValue),
        keyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = background,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Red
        ),
        isError = errorCondition,
        textStyle = textStyle,
        modifier = modifier
    )
}

@Preview
@Composable
fun CurrencyTextFieldTransactionPreview() {
    CurrencyTextField(
        value = 100.toString(),
        onValueChange = { },
        positiveValue = true,
        textColor = Green,
        textStyle = TextStyle.Default.copy(
            fontSize = 36.sp,
            textAlign = TextAlign.End
        ),
        modifier = Modifier.requiredWidth(200.dp))
}

@Preview
@Composable
fun CurrencyTextFieldBudgetPreview() {
    CurrencyTextField(
        value = 100.toString(),
        onValueChange = { },
        positiveValue = true,
        textColor = Black,
        modifier = Modifier.requiredWidth(100.dp),
        textStyle = TextStyle.Default.copy(
            textAlign = TextAlign.Center
        )
    )
}