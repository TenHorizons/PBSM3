package com.example.pbsm3.ui.commonScreenComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.ui.commonScreenComponents.datepicker.PBSDatePicker
import com.example.pbsm3.ui.navhost.Screen
import com.example.pbsm3.ui.theme.PBSM3Theme
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PBSTopBar(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit = {},
    budgetItemName: String = "MISSING_BUDGET_ITEM_NAME",
    onNavigateUp: () -> Unit = {},
    screen: Screen
) {
    val hideScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pinScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if(screen == Screen.Budget) CenterAlignedTopAppBar(
        title = { PBSDatePicker(screen = Screen.Budget, onDateSelected = onDateSelected)},
        modifier = modifier.nestedScroll(hideScrollBehavior.nestedScrollConnection),
        scrollBehavior = hideScrollBehavior
    )else TopAppBar(
        title = {
            Text(text =
            when (screen) {
                Screen.AddTransaction -> "Add Transaction"
                Screen.BudgetItem -> budgetItemName
                Screen.Account -> "Accounts"
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