package com.example.pbsm3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pbsm3.data.defaultAccounts
import com.example.pbsm3.model.Account
import com.example.pbsm3.ui.screens.accounts.AccountScreenState
import com.example.pbsm3.ui.screens.accounts.AccountScreenViewModel
import com.example.pbsm3.theme.PBSM3Theme

@Composable
fun AccountsScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(8.dp)
    ) {
        item {
            Header(uiState = uiState)
        }
        item {
            AccountItems(accounts = defaultAccounts)
        }
    }
}

@Composable
fun Header(uiState: AccountScreenState) {
    Row(modifier = Modifier.padding(horizontal = 16.dp,vertical = 8.dp)) {
        Text("Total")
        Spacer(Modifier.weight(1f))
        Text(text = uiState.accountList.sumOf { it.balance }.toString())
    }
}

@Composable
fun AccountItems(accounts: List<Account>) {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for(account in accounts){
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(text = account.name)
                    Spacer(Modifier.weight(1f))
                    Text(text = account.balance.toString())
                }
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    PBSM3Theme {
        AccountsScreen()
    }
}