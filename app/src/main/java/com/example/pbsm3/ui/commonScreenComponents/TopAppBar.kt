package com.example.pbsm3.ui.commonScreenComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pbsm3.ui.navhost.Screen
import com.example.pbsm3.ui.theme.PBSM3Theme
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PBSTopBar(
    modifier: Modifier = Modifier,
    onDatePicked: (Date) -> Unit = {},
    budgetItemName: String = "MISSING_BUDGET_ITEM_NAME",
    onNavigateUp: () -> Unit = {},
    screen: Screen
) {
    val hideScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pinScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if(screen == Screen.Budget) CenterAlignedTopAppBar(
        title = {Text("Date Picker Here")},
        modifier = modifier.nestedScroll(hideScrollBehavior.nestedScrollConnection),
        scrollBehavior = hideScrollBehavior
    )else TopAppBar(
        title = {
            Text(text =
            when (screen) {
                Screen.Transaction -> "Add Transaction"
                Screen.BudgetItem -> budgetItemName
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
    /*Taken from Material Design's TopAppBar composable with
    modifications.*/
    /*Surface(
        shape = RectangleShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = BottomAppBarDefaults.ContainerElevation,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (screen == Screen.Budget) {
                Spacer(Modifier.weight(1f))
                //TODO: MonthYear picker implementation here.
                Text("Date Picker Here!")
                Spacer(Modifier.weight(1f))
            } else {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back to Previous Screen")
                }
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = when (screen) {
                        Screen.Transaction -> "Add Transaction"
                        Screen.BudgetItem -> budgetItemName
                        else -> "ERROR SELECTING TITLE"
                    })
            }
        }
    }*/
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
        PBSTopBar(screen = Screen.Transaction)
    }
}