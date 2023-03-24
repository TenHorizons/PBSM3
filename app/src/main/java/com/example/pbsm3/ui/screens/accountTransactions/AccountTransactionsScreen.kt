package com.example.pbsm3.ui.screens.accountTransactions

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

private const val TAG = "AccountTransactionsScreen"

@Composable
fun AccountTransactionsScreen(
    //TODO: convert to account Firebase doc ID
    accountName:String = ""
) {
    Column {
        Log.d(TAG, "Account Transactions Screen composed. Account name: $accountName")
        Text("Account Transactions Screen")
    }
    //TODO: Account Transactions Screen
}