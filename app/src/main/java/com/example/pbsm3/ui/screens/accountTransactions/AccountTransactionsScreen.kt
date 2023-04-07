package com.example.pbsm3.ui.screens.accountTransactions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pbsm3.data.ALL_ACCOUNTS
import com.example.pbsm3.model.Transaction
import com.example.pbsm3.theme.PBSM3Theme

private const val TAG = "AccountTransactionsScreen"

@Composable
fun AccountTransactionsScreen(
    //TODO: convert to account Firebase doc ID
    accountName: String = "",
    onBackPressed: () -> Unit = {}
) {
    BackHandler(onBack = onBackPressed)
    LazyColumn {
        /*item {
            Header()
        }*/
        items(
            //TODO: get transactions from Firebase using account doc ID
            listOf<Transaction>()
        ) {
            Transaction(it,accountName)
        }
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

@Composable
fun Transaction(transaction: Transaction,accountName: String) {
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
                if(accountName == ALL_ACCOUNTS){
                    Text(
                        //TODO fill with account name from Firebase (maybe use ref)
                        text = "some account",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
                Text(
                    text = transaction.date.toString(),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                Text(
                    text = "to properly display",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "RM${String.format("%.2f", transaction.amount)}",
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }
        Divider()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountTransactionsScreenPreview() {
    PBSM3Theme {
        AccountTransactionsScreen(accountName = "Default Account")
    }
}