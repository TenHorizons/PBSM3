package com.example.pbsm3.ui.screens.accounts

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.data.ALL_ACCOUNTS
import com.example.pbsm3.data.displayTwoDecimal
import com.example.pbsm3.model.Account
import com.example.pbsm3.theme.PBSM3Theme
import com.maxkeppeker.sheets.core.icons.filled.ChevronRight

private const val TAG = "AccountsScreen"

@Composable
fun AccountsScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountScreenViewModel = hiltViewModel(),
    onAccountClicked:(Account)->Unit = {},
    onBackPressed:()->Unit ={}
) {
    BackHandler(onBack = onBackPressed)
    Log.d(TAG,"account screen starting. loading accounts.")
    viewModel.readAccountRepository()
    Log.d(TAG,"accounts loaded: ${viewModel.uiState.value.accounts}")
    val uiState by viewModel.uiState
    if(uiState.accounts.isEmpty()){
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.3F))
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                text = "No Accounts!\nAdd Account Now!",
                style = typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
        return
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(8.dp)
    ) {
        item {
            AllAccountTransactions(onClick = {
                Log.d(TAG, "All accounts card/button clicked.")
                onAccountClicked(Account(
                    id = ALL_ACCOUNTS,
                    name = ALL_ACCOUNTS
                ))
            })
        }
        items(viewModel.getAccounts()) {
            IndividualAccount(
                account = it,
                onClick = { account ->
                    Log.d(TAG, "account ${account.name} card/button clicked.")

                    onAccountClicked(account)
                }
            )
        }
    }
}

@Composable
fun AllAccountTransactions(
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("All Account Transactions")
            Spacer(Modifier.weight(1f))
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.size(32.dp),
                onClick = onClick
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ChevronRight,
                    contentDescription = "See transactions for all accounts."
                )
            }
        }
    }
}

@Composable
fun IndividualAccount(
    account: Account,
    onClick: (Account) -> Unit
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        modifier = Modifier.clickable { onClick(account) }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = account.name)
            Spacer(Modifier.weight(1f))
            Column {
                Text("Balance:")
                Text(
                    text = "RM${account.balance.displayTwoDecimal()}")
            }
            Spacer(Modifier.padding(horizontal = 8.dp))
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.size(32.dp),
                onClick = { onClick(account) }
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ChevronRight,
                    contentDescription = "See transactions for ${account.name} account."
                )
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