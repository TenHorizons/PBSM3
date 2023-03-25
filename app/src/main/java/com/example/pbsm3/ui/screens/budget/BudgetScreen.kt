package com.example.pbsm3.ui.screens.budget

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pbsm3.data.defaultBudget
import com.example.pbsm3.data.getFirstDayOfMonth
import com.example.pbsm3.model.Budget
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Category
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.currencytextfield.CurrencyTextField
import java.time.LocalDate


private const val TAG = "BudgetScreen"

@Composable
fun BudgetScreen(
    modifier: Modifier = Modifier,
    budget: Budget,
    date: LocalDate,
    viewModel: BudgetScreenViewModel = viewModel(),
    onItemClicked: (Category, BudgetItem) -> Unit = {_,_->},
    onBackPressed:()->Unit={}
) {
    BackHandler(onBack = onBackPressed)
    val uiState by viewModel.uiState.collectAsState()
    Log.d(
        TAG, "BudgetScreen start. old budget equals new: " +
            "${uiState.budget == defaultBudget}")
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        AvailableToBudgetRow(
            //TODO: configure available to budget after Transaction Screen completed.
            availableToBudget = 0.0,
            modifier = Modifier.padding(top = 8.dp))
        LazyColumn(Modifier.fillMaxWidth()) {
            items(viewModel.getCategoriesByDate(date)) {
                CategoryCard(
                    category = it,
                    onItemClicked = onItemClicked,
                    onItemChanged = {category, item, index->
                        viewModel.updateBudgetItem(category,item,index)
                        Log.d(
                            TAG, "updateBudgetItem. old budget equals new: " +
                                "${uiState.budget == defaultBudget}")
                    })
            }
        }
    }
}

@Composable
fun AvailableToBudgetRow(
    availableToBudget: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.tertiaryContainer,
        )) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = "Available to Budget: ")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = String.format("RM%.2f", availableToBudget),
                color = colorScheme.onTertiaryContainer,
                style = TextStyle.Default.copy(
                    fontSize = 36.sp,
                    textAlign = TextAlign.End
                ))
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onItemClicked: (Category,BudgetItem) -> Unit,
    onItemChanged: (Category,BudgetItem,Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Row(
                Modifier
                    .padding(start = 4.dp)
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 4.dp)
                )
                Column(Modifier.weight(0.225f)) {
                    Text(text = "Budgeted")
                    Text(
                        text =
                        "RM${String.format("%.2f", category.totalBudgeted)}",
                        textAlign = TextAlign.End)
                }
                Column(Modifier.weight(0.225f)) {
                    Text(text = "Available")
                    Text(
                        text =
                        "RM${String.format("%.2f", category.totalAvailable)}",
                        textAlign = TextAlign.End)
                }
                Icon(
                    imageVector =
                    if (expanded) Icons.Filled.ExpandLess
                    else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    Modifier.weight(0.05f))
            }
            if (expanded) {
                BudgetItemRow(
                    category.items,
                    onBudgetItemChanged = { item, index -> onItemChanged(category, item, index) },
                    onItemClicked = { onItemClicked(category, it) }
                )
            }
        }
    }
}

@Composable
fun BudgetItemRow(
    budgetItems: List<BudgetItem>,
    onBudgetItemChanged: (BudgetItem,Int) -> Unit,
    onItemClicked: (BudgetItem) -> Unit
) {
    for (item in budgetItems) {
        Card {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    item.name,
                    Modifier
                        .weight(0.5f)
                        .padding(start = 4.dp)
                        .clickable(onClick = { onItemClicked(item) })
                )
                CurrencyTextField(
                    value =
                    if (item.budgeted == 0.0) ""
                    else String.format("%.0f", item.budgeted * 100),
                    onValueChange = {
                        onBudgetItemChanged(
                            if (it == "") item.copy(budgeted = 0.0)
                            else item.copy(budgeted = it.toDouble() / 100),
                            budgetItems.indexOf(item)
                        )
                    },
                    background = colorScheme.surfaceVariant,
                    isPositiveValue = true,
                    textColor = colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .weight(0.225f),
                    textStyle = TextStyle.Default.copy(
                        textAlign = TextAlign.Center
                    )
                )
                //TODO: change Available field to normal Text since it's read only.
                CurrencyTextField(
                    value =
                    if (item.available == 0.0) ""
                    else String.format("%.0f", item.available * 100),
                    onValueChange = {
                        onBudgetItemChanged(
                            if (it == "") item.copy(available = 0.0)
                            else item.copy(available = it.toDouble() / 100),
                            budgetItems.indexOf(item)
                        )
                    },
                    background = colorScheme.surfaceVariant,
                    isPositiveValue = true,
                    textColor = colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .weight(0.225f),
                    textStyle = TextStyle.Default.copy(
                        textAlign = TextAlign.End
                    )
                )
                Spacer(modifier = Modifier.weight(0.05f))
            }
            Divider()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BudgetScreenLightThemePreview() {
    PBSM3Theme {
        BudgetScreen(budget = defaultBudget, date = getFirstDayOfMonth())
    }
}