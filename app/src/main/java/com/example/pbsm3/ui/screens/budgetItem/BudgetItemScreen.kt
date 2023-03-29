@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3.ui.screens.budgetItem

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.theme.PBSM3Theme
import java.math.BigDecimal
import java.time.LocalDate

private  const val TAG = "BudgetItemScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetItemScreen(
    //TODO: convert to Firebase doc ID
    modifier: Modifier = Modifier,
    budgetItemName: String = "defaultBudgetItem",
    onBackPressed:()->Unit={},
    viewModel: BudgetItemScreenViewModel = hiltViewModel()
){
    BackHandler(onBack = onBackPressed)
    //Log.d(TAG,"Budget Item Screen composed. Budget Item Name: $budgetItemName")

    //TODO use doc ID to get item, then initialize state.
    viewModel.setupState(
        budgetItemName = budgetItemName,
        totalCarryover = BigDecimal("200"),
        totalBudgeted = BigDecimal("100"),
        totalExpenses = BigDecimal("120"),
        date = LocalDate.now()
    )
    val uiState: BudgetItemScreenState by viewModel.uiState
    Column {
        Card(modifier = modifier.padding(8.dp)) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = budgetItemName,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
                Divider(thickness = 3.dp)
                RowItem(
                    item = "Cash left over from ${uiState.date.minusMonths(1).month.name}",
                    amount = uiState.totalCarryover)
                RowItem(
                    item = "Budgeted this month",
                    amount = uiState.totalBudgeted)
                RowItem(
                    item = "Cash spent this month",
                    amount = uiState.totalExpenses)
                Divider(thickness = 3.dp)
                Row {
                    Text(text = "Available")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "RM${String.format("%.2f", viewModel.getAvailable())}")
                }
            }
        }
        Card(modifier = modifier.padding(8.dp)) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Goals",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
                Divider(thickness = 3.dp)
                Button(onClick = { /*TODO Implement Goal*/ }) {
                    Text(text = "Create a Goal")
                }
            }
        }
        Memo(modifier)
    }
}

@Composable
fun RowItem(item:String,amount:BigDecimal) {
    Row{
        Text(text = item)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "RM${String.format("%.2f",amount)}")
    }
}

@Composable
fun Memo(modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(8.dp)){
        //TODO Implement memo text (memo should be the same for all months)
        var memoText by remember{ mutableStateOf("")}
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "Notes",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize
            )
            Divider(thickness = 3.dp)
            TextField(
                value = memoText,
                onValueChange = { memoText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(300.dp),
                colors = TextFieldDefaults.textFieldColors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red
                ),
                label = { Text("Write notes here...") }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetItemScreenPreview(){
    PBSM3Theme {
        BudgetItemScreen()
    }
}