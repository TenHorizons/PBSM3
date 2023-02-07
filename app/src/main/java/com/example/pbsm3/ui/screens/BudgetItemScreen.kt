package com.example.pbsm3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.data.defaultBudgetItem
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.ui.theme.PBSM3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetItemScreen(onNavigateUp:()->Unit, budgetItem: BudgetItem, modifier: Modifier = Modifier){
    Column(modifier = modifier){
        Row{
            Text(text = "Name:")
            Spacer(modifier = Modifier.weight(1f))
            TextField(
                value = "budget item",
                onValueChange = {},
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer),
                singleLine = true
            )
        }
        Row{
            Text(text = "")
            //TODO: Continue work after Transaction screen completed.
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetItemScreenPreview(){
    PBSM3Theme() {
        BudgetItemScreen(budgetItem = defaultBudgetItem, onNavigateUp = {})
    }
}