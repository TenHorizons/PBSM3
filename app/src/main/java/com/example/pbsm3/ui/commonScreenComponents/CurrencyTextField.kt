package com.example.pbsm3.ui.commonScreenComponents

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
    defaultMinWidth:Dp = 10.dp
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = CurrencyAmountInputVisualTransformation
            (positiveValue = positiveValue),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType
            .NumberPassword),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = background,
            textColor = textColor,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Red
        ),
        isError = errorCondition,
        textStyle = textStyle,
        modifier = modifier
    )

    /*Default Min Size for TextField wasn't being overridden correctly, so I
     implemented a BasicTextField*/
    /*TextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = textStyle,
        visualTransformation =
        CurrencyAmountInputVisualTransformation(
            positiveValue = positiveValue),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = background,
            textColor = textColor,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Red
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        modifier = modifier,

    )*/

    //moved down from arguments to not clutter the space.
    /*val enabled = true
    val readOnly = false
    val label: @Composable (() -> Unit)? = null
    val placeholder: @Composable (() -> Unit)? = null
    val leadingIcon: @Composable (() -> Unit)? = null
    val trailingIcon: @Composable (() -> Unit)? = null
    val visualTransformation: VisualTransformation =
        CurrencyAmountInputVisualTransformation(positiveValue = positiveValue)
    val keyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
    val keyboardActions = KeyboardActions()
    val singleLine = true
    val maxLines: Int = Int.MAX_VALUE
    val interactionSource: MutableInteractionSource =
        remember { MutableInteractionSource() }
    val shape: Shape = MaterialTheme.shapes.small.copy(
        bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize)
    val colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = background,
        textColor = textColor,
        disabledIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Red
    )*/

    // If color is not provided via the text style, use content color as a default
    /*val txtColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }*/
    /*val mergedTextStyle = textStyle.merge(TextStyle(color = txtColor))

    BasicTextField(
        value = value,
        modifier = modifier
            .background(colors.backgroundColor(enabled).value, shape)
            .indicatorLine(enabled, errorCondition, interactionSource, colors)
            .defaultMinSize(
                minWidth = defaultMinWidth,
                minHeight = TextFieldDefaults.MinHeight)
        ,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(errorCondition).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = errorCondition,
                interactionSource = interactionSource,
                colors = colors
            )
        }
    )*/
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