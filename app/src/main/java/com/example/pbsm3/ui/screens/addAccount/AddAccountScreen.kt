package com.example.pbsm3.ui.screens.addAccount

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.theme.PBSM3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    modifier: Modifier = Modifier,
    onBackPressed:()->Unit ={},
    viewModel: AddAccountScreenViewModel = hiltViewModel(),
    onAddAccountComplete:()->Unit = {}
) {
    BackHandler(onBack = onBackPressed)

    val uiState:AddAccountScreenState by viewModel.uiState
    var isError by remember { mutableStateOf(false) }
    var isAdded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("")}

    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = "Account Name:",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        TextField(
            value = uiState.accountName,
            onValueChange = {viewModel.onAccountNameChange(it)},
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                disabledIndicatorColor = Transparent,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Account Balance:",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        TextField(
            value = viewModel.getAccountBalance(),
            onValueChange = { viewModel.onBalanceChange(it) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                disabledIndicatorColor = Transparent,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            ),
            keyboardOptions =
            //TODO Upgrade to Decimal
            KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.End
        ){
            var isInProgress by remember { mutableStateOf(false) }
            Button(onClick = {
                isInProgress = true
                viewModel.onAddAccount(
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
            }) {
                if(isInProgress){
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                }else {
                    Text("Add Account")
                }
            }
        }
        if (isError || isAdded) {
            Card(
                colors =
                CardDefaults.cardColors(
                    containerColor =
                    if (isError) MaterialTheme.colorScheme.errorContainer
                    else MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text =
                        if (isError) "Error! Error Message:\n$errorMessage"
                        else "Account Added!"
                    )
                }
                viewModel.hideAfterDelay(onComplete = {
                    if(isAdded) onAddAccountComplete()
                    isError = false
                    isAdded = false
                    errorMessage = ""
                })
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddAccountScreenPreview() {
    PBSM3Theme {
        AddAccountScreen()
    }
}