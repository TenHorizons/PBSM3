package com.example.pbsm3.ui.screens.budgetItem

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.theme.PBSM3Theme

private  const val TAG = "BudgetItemScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetItemScreen(
    //TODO: convert to Firebase doc ID
    budgetItemName: String,
    modifier: Modifier = Modifier
){
    Log.d(TAG,"Budget Item Screen composed. Budget Item Name: $budgetItemName")
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetItemScreenPreview(){
    PBSM3Theme {
        BudgetItemScreen(budgetItemName = "defaultBudgetItem")
    }
}