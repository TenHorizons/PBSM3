package com.example.pbsm3.ui.screens.accountTransactions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.data.ALL_ACCOUNTS
import com.example.pbsm3.data.displayTwoDecimal
import com.example.pbsm3.data.isLessThanZero
import com.example.pbsm3.firebaseModel.Transaction
import com.example.pbsm3.theme.PBSM3Theme

private const val TAG = "AccountTransactionsScreen"

@Composable
fun AccountTransactionsScreen(
    modifier:Modifier = Modifier,
    viewModel: AccountTransactionsScreenViewModel = hiltViewModel(),
    accountRef: String = "",
    onBackPressed: () -> Unit = {}
) {
    BackHandler(onBack = onBackPressed)

    LaunchedEffect(accountRef){
        viewModel.loadTransactions(accountRef)
        viewModel.addListeners()
    }

    val uiState by viewModel.uiState

    if(uiState.transactions.isEmpty()){
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
                text = "No Transactions!\nAdd Transactions Now!",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
        return
    }
    LazyColumn {
        /*item {
            Header()
        }*/
        items(
            uiState.transactions
        ) {transaction ->
            TransactionRow(
                transaction = transaction,
                accountRef =  accountRef,
                onGetAccountNameByRef = {
                    viewModel.getAccountNameByRef(it)
                },
                getAssignedToNameByRef = {
                    viewModel.getAssignedToNameByRef(it)
                }
            )
        }
    }
}

@Composable
fun TransactionRow(
    transaction: Transaction,
    accountRef: String,
    onGetAccountNameByRef: (String) -> String,
    getAssignedToNameByRef: (String) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CutCornerShape(0.dp),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                if(accountRef == ALL_ACCOUNTS){
                    Text(
                        text = onGetAccountNameByRef(transaction.accountRef),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
                Text(
                    text = transaction.date.toString(),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                Text(
                    text = getAssignedToNameByRef(transaction.assignedTo_Ref),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text =
                (if(transaction.amount.isLessThanZero())"-RM"
                else "RM") +
                        "${transaction.amount.displayTwoDecimal().abs()}",
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }
        Divider(color = colorScheme.outline)
    }
}

@Composable
fun Header() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = CutCornerShape(0.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Some Header")
            }
        }
        Divider()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountTransactionsScreenPreview() {
    PBSM3Theme {
        AccountTransactionsScreen(accountRef = "Default Account")
    }
}