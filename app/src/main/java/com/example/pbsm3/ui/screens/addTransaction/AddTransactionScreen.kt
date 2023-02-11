package com.example.pbsm3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pbsm3.R
import com.example.pbsm3.ui.commonScreenComponents.currencytextfield.CurrencyTextField
import com.example.pbsm3.ui.commonScreenComponents.datepicker.PBSDatePicker
import com.example.pbsm3.ui.navhost.Screen
import com.example.pbsm3.theme.PBSM3Theme

private const val TAG = "TransactionScreen"

@Composable
fun AddTransactionScreen(modifier: Modifier = Modifier) {
    var amount by remember { mutableStateOf(0.0) }
    var memoText by remember { mutableStateOf("") }

    var switchFlipped by remember { mutableStateOf(true) }
    var displayedAmount by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        AmountRow(
            switchFlipped = switchFlipped,
            displayedAmount = displayedAmount,
            onCheckedChange = { switchFlipped = it },
            onValueChange = {
                displayedAmount =
                    if (it.startsWith("0")) ""
                    else it
            }
        )
        Card(modifier = Modifier.padding(8.dp)) {
            TransactionInfo()
        }
        Card(modifier = Modifier.padding(8.dp)) {
            Memo(memoText, onTextChanged = {})
        }
    }
}

@Composable
fun AmountRow(
    switchFlipped: Boolean,
    displayedAmount: String,
    onCheckedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            if (switchFlipped) colorScheme.tertiaryContainer
            else colorScheme.errorContainer
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = switchFlipped,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Green,
                    uncheckedTrackColor = Red
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .testTag(stringResource(R.string.amount_switch))
            )
            Spacer(modifier = Modifier.weight(1f))
            CurrencyTextField(
                value = displayedAmount,
                onValueChange = onValueChange,
                background =
                if (switchFlipped) colorScheme.tertiaryContainer
                else colorScheme.errorContainer,
                positiveValue = switchFlipped,
                textStyle = TextStyle.Default.copy(
                    fontSize = 36.sp,
                    textAlign = TextAlign.End,
                    shadow = Shadow(Color.Black, blurRadius = 0.3f)
                ),
                defaultMinWidth = 100.dp
            )
        }
    }
}

@Composable
fun TransactionInfo() {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Column {
        TransactionInfoItemWithMenu(
            title = "Category", selectedOption = selectedOptionText,
            onSelectedChange = { selectedOptionText = it },
            options = options)
        Divider()
        TransactionInfoItemWithMenu(
            title = "Account", selectedOption = selectedOptionText,
            onSelectedChange = { selectedOptionText = it },
            options = options)
        Divider()
        Row(
            Modifier.padding(start = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Date", textAlign = TextAlign.Start)
            Spacer(Modifier.weight(0.5f))
            PBSDatePicker(
                modifier = Modifier.weight(1f),
                screen = Screen.AddTransaction,
                onDateSelected = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionInfoItemWithMenu(
    title: String,
    selectedOption: String,
    onSelectedChange: (String) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Row(Modifier.padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, textAlign = TextAlign.Start)
        Spacer(modifier = Modifier.weight(1f))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedOption,
                onValueChange = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    disabledIndicatorColor = Transparent,
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent,
                    errorIndicatorColor = Red
                ),
                modifier = Modifier.widthIn(100.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onSelectedChange.invoke(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Memo(memoText: String, onTextChanged: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(8.dp)
    ) {
        Text("Memo", Modifier.padding(start = 14.dp))
        TextField(
            value = memoText,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            colors = TextFieldDefaults.textFieldColors(
                textColor = White,
                disabledIndicatorColor = Transparent,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                errorIndicatorColor = Red
            ),
            label = { Text("Write notes here...") }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionScreenPreview() {
    PBSM3Theme {
        AddTransactionScreen()
    }
}