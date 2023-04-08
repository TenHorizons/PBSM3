package com.example.pbsm3.ui.screens.addTransaction

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.R
import com.example.pbsm3.Screen
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.currencytextfield.CurrencyTextField
import com.example.pbsm3.ui.commonScreenComponents.datepicker.PBSDatePicker
import java.time.LocalDate

private const val TAG = "AddTransactionScreen"

@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: AddTransactionScreenViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {}
) {
    BackHandler(onBack = onBackPressed)
    val uiState by viewModel.uiState
    var isError by remember { mutableStateOf(false) }
    var isAdded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        AmountRow(
            onAmountChange = { amount, switchGreen ->
                viewModel.onAmountChange(amount, switchGreen)
            }
        )
        Card(modifier = Modifier.padding(8.dp)) {
            TransactionInfo(
                categoryOptions = viewModel.getCategoryOptions(),
                accountOptions = viewModel.getAccountOptions(),
                onCategorySelected = { viewModel.onCategoryChange(it) },
                onAccountSelected = { viewModel.onAccountChange(it) },
                onDateSelected = { viewModel.onDateChange(it) }
            )
        }
        Card(modifier = Modifier.padding(8.dp)) {
            Memo(
                uiState.memoText,
                onTextChanged = { viewModel.onMemoChange(it) })
        }
        Row(
            Modifier.padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Spacer(modifier = Modifier.weight(1f))
            var isInProgress by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    isInProgress = true
                    viewModel.onAddTransaction(
                        onError = {
                            isError = true
                            errorMessage = it.toString()
                            isInProgress = false
                        },
                        onComplete = {
                            isInProgress = false
                            isAdded = true
                        }
                    )
                }
            ) {
                if(isInProgress){
                    CircularProgressIndicator(color = colorScheme.background)
                }else{
                    Text(text = "Add Transaction")
                }
            }
        }
        if (isError || isAdded) {
            Card(
                colors =
                CardDefaults.cardColors(
                    containerColor =
                    if (isError) colorScheme.errorContainer
                    else colorScheme.tertiaryContainer
                )
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text =
                        if (isError) "Error! Error Message:\n$errorMessage"
                        else "Transaction Added!"
                    )
                }
                viewModel.hideAfterDelay(onComplete = {
                    isError = false
                    isAdded = false
                    errorMessage = ""
                })
            }
        }
    }
}

@Composable
fun AmountRow(
    onAmountChange: (String, Boolean) -> String
) {
    var displayedAmount by remember { mutableStateOf("") }
    var switchGreen by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            if (switchGreen) colorScheme.tertiaryContainer
            else colorScheme.errorContainer
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = switchGreen,
                onCheckedChange = { switchGreen = !switchGreen },
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
                onValueChange = {
                    Log.d(TAG, "$TAG AmountRow value changed. TextField return value: $it")
                    displayedAmount = onAmountChange(it, switchGreen)
                },
                background =
                if (switchGreen) colorScheme.tertiaryContainer
                else colorScheme.errorContainer,
                isPositiveValue = switchGreen,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 36.sp,
                    textAlign = TextAlign.End,
                    shadow = Shadow(Color.Black, blurRadius = 0.3f)
                )
            )
        }
    }
}

@Composable
fun TransactionInfo(
    categoryOptions: List<String>,
    accountOptions: List<String>,
    onCategorySelected: (String) -> Unit,
    onAccountSelected: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(categoryOptions[0]) }
    var selectedAccount by remember { mutableStateOf(accountOptions[0]) }

    Column {
        TransactionInfoItemWithMenu(
            title = "Category", selectedOption = selectedCategory,
            onSelectedChange = {
                selectedCategory = it
                onCategorySelected(it)
            },
            options = categoryOptions)
        Divider()
        TransactionInfoItemWithMenu(
            title = "Account", selectedOption = selectedAccount,
            onSelectedChange = {
                selectedAccount = it
                onAccountSelected(it)
            },
            options = accountOptions)
        Divider()
        Row(
            Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Date", textAlign = TextAlign.Start)
            Spacer(Modifier.weight(1f))
            PBSDatePicker(
                modifier = Modifier.widthIn(100.dp),
                screen = Screen.AddTransaction,
                onDateSelected = onDateSelected)
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
    Row(
        Modifier.padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, textAlign = TextAlign.Start)
        Spacer(modifier = Modifier.weight(1f))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .widthIn(100.dp),
                readOnly = true,
                value = selectedOption,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.End
                ),
                onValueChange = {/*no action here, since read only.*/ },
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
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onSelectedChange(option)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
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
fun AddTransactionScreenPreview() {
    PBSM3Theme {
        AddTransactionScreen()
    }
}