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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

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
    var errorType by remember { mutableStateOf("")}
    val coroutineScope = rememberCoroutineScope()

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
            Button(onClick = {
                if(uiState.accountName == "" ||
                    (uiState.accountBalance.compareTo(BigDecimal("0")))==0){
                    //TODO add snackbar "please input", or add animation
                    coroutineScope.launch {
                        isError = true
                        errorType = "Please input values for account name and balance."
                        delay(2000)
                        isError = false
                        errorType = ""
                    }
                }else {
                    viewModel.onAddAccount()
                    onAddAccountComplete()
                }
            }) {
                Text("Add Account")
            }
        }
        //TODO remove once snackbar added
        if(isError){
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(Modifier.padding(8.dp)) {
                    Text(errorType)
                }
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