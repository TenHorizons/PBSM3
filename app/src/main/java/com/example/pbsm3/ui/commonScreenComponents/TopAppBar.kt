package com.example.pbsm3.ui.commonScreenComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.Screen
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.datepicker.PBSDatePicker
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PBSTopBar(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    screen: Screen,
    onActionClicked: () -> Unit = {},
    customTopBarText: String = ""
) {
    val hideScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pinScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    when (screen) {
        Screen.Login, Screen.Splash -> return
        Screen.Budget -> CenterAlignedTopAppBar(
            title = { PBSDatePicker(screen = Screen.Budget, onDateSelected = onDateSelected) },
            modifier = modifier.nestedScroll(hideScrollBehavior.nestedScrollConnection),
            scrollBehavior = hideScrollBehavior
        )
        Screen.AddTransaction, Screen.Accounts -> TopAppBar(
            title = {
                Text(
                    when (screen) {
                        Screen.Accounts -> "Accounts"
                        Screen.AddTransaction -> "Add Transaction"
                        else -> "ERROR"
                    })
            },
            modifier = modifier.nestedScroll(pinScrollBehavior.nestedScrollConnection),
            scrollBehavior = pinScrollBehavior,
            actions = {
                if (screen == Screen.Accounts) {
                    Button(onClick = onActionClicked) {
                        Text(text = "Add Account")
                    }
                }
            }
        )
        else -> TopAppBar(
            title = {
                Text(
                    text =
                    when (screen) {
                        Screen.BudgetItem ->
                            if (customTopBarText != "") customTopBarText
                            else "ERROR Budget Item Name"
                        Screen.AddAccountScreen -> "Add Account"
                        Screen.AccountTransactions ->
                            if (customTopBarText != "") "$customTopBarText Transactions"
                            else "ERROR Account Name Transactions"
                        else -> "ERROR SELECTING TITLE"
                    }
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back to Previous Screen")
                }
            },
            modifier = modifier.nestedScroll(pinScrollBehavior.nestedScrollConnection),
            scrollBehavior = pinScrollBehavior
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetTopBarPreview() {
    PBSM3Theme {
        PBSTopBar(screen = Screen.Budget)
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetItemTopBarPreview() {
    PBSM3Theme {
        PBSTopBar(screen = Screen.BudgetItem)
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionTopBarPreview() {
    PBSM3Theme {
        PBSTopBar(screen = Screen.AddTransaction)
    }
}

@Preview
@Composable
fun AccountTopBarPreview() {
    PBSM3Theme {
        PBSTopBar(screen = Screen.Accounts)
    }
}

@Preview
@Composable
fun AccountTransactionsTopBarPreview() {
    PBSM3Theme {
        PBSTopBar(screen = Screen.AccountTransactions)
    }
}

@Preview
@Composable
fun AddAccountTopBarPreview() {
    PBSM3Theme {
        PBSTopBar(screen = Screen.AddAccountScreen)
    }
}