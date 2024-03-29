@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.pbsm3.ui.screens.budget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.data.*
import com.example.pbsm3.firebaseModel.BudgetItem
import com.example.pbsm3.firebaseModel.Category
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.currencytextfield.CurrencyTextField
import java.math.BigDecimal
import java.time.LocalDate


private const val TAG = "BudgetScreen"
//TODO deal with budget name eventually
@Composable
fun BudgetScreen(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    viewModel: BudgetScreenViewModel = hiltViewModel(),
    onItemClicked: (Category, BudgetItem) -> Unit = { _, _ -> },
    onBackPressed: () -> Unit = {}
) {
    BackHandler(onBack = onBackPressed)

    LaunchedEffect(true) {
        viewModel.registerListeners()
        viewModel.setSelectedDate(selectedDate)
    }
    LaunchedEffect(selectedDate) {
        viewModel.setSelectedDate(selectedDate)
    }
    val uiState by viewModel.uiState

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        AvailableToBudgetRow(
            availableToBudget = viewModel.getUnassignedValueToDisplay(),
            modifier = Modifier.padding(top = 8.dp))
        LazyColumn(Modifier.fillMaxWidth()) {
            items(uiState.categoryItemMapping.keys.sortedBy { it.position }) {
                CategoryCard(
                    category = it,
                    onItemClicked = onItemClicked,
                    onItemChanged = { category, item ->
                        viewModel.updateBudgetItemDisplay(category, item)
                    },
                    onGettingCategoryItems = { category ->
                        uiState.categoryItemMapping[category]
                            ?.sortedBy { item->item.position } ?: listOf()
                    },
                    onDone = { category, item ->
                        keyboardController?.hide()
                        viewModel.updateBudgetItem(category, item)
                    })
            }
        }
    }
}

@Composable
fun AvailableToBudgetRow(
    availableToBudget: BigDecimal,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState(0)
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor =
            if(availableToBudget.isLessThanZero()) colorScheme.errorContainer
            else colorScheme.tertiaryContainer,
        )) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = "Available to Budget: ")
            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.horizontalScroll(scroll),
                text =
                (if(availableToBudget.isLessThanZero())"-RM"
                else "RM") +
                "${availableToBudget.displayTwoDecimal().abs()}",
                color =
                if(availableToBudget.isLessThanZero()) colorScheme.onErrorContainer
                else colorScheme.onTertiaryContainer,
                style = TextStyle.Default.copy(
                    fontSize = 36.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onItemClicked: (Category, BudgetItem) -> Unit,
    onItemChanged: (Category, BudgetItem) -> Unit,
    onGettingCategoryItems: (Category) -> List<BudgetItem>,
    onDone: (Category, BudgetItem) -> Unit
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
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.weight(0.45f)
                )
                Column(Modifier.weight(0.25f)) {
                    Text(text = "Budgeted")
                    Text(
                        text =
                        "RM${category.totalBudgeted.displayTwoDecimal()}",
                        textAlign = TextAlign.End)
                }
                Column(Modifier.weight(0.25f)) {
                    Text(text = "Available")
                    Text(
                        text =
                        "RM${category.getCarryover().displayTwoDecimal()}",
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
                    onGettingCategoryItems(category),
                    onBudgetItemChanged = { item -> onItemChanged(category, item) },
                    onItemClicked = { onItemClicked(category, it) },
                    onDone = { onDone(category, it) }
                )
            }
            Divider()
        }
    }
}

@Composable
fun BudgetItemRow(
    budgetItems: List<BudgetItem>,
    onBudgetItemChanged: (BudgetItem) -> Unit,
    onItemClicked: (BudgetItem) -> Unit,
    onDone: (BudgetItem) -> Unit
) {
    var wasFocused by remember { mutableStateOf(false)}
    for (item in budgetItems) {
        Card {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier
                        .weight(0.45f)
                        .clickable(onClick = { onItemClicked(item) }),
                    //softWrap = false
                )
                CurrencyTextField(
                    modifier = Modifier.weight(0.25f),
                    value =
                    if (item.totalBudgeted.isZero()) ""
                    else item.totalBudgeted.toDigitString(),
                    onValueChange = {
                        onBudgetItemChanged(
                            //currencyTextField seems to remove a few decimal places,
                            //making it less accurate, but it probably wouldn't
                            //be an issue.
                            if (it == "") item.copy(totalBudgeted = BigDecimal.ZERO)
                            else item.copy(totalBudgeted = it.fromDigitString())

                        )
                    },
                    onDone = { onDone(item) },
                    background = colorScheme.surfaceVariant,
                    isPositiveValue = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = typography.bodyLarge.fontSize,
                        textAlign = TextAlign.Start,
                        color = colorScheme.onSurface
                    )
                )
                //TODO: make into a small color-changing composable eventually
                Text(
                    text =
                    (if (item.getCarryover().isLessThanZero()) "-RM" else "RM")
                            + (item.getCarryover().displayTwoDecimal()),
                    color =
                    if (item.getCarryover().isLessThanZero())
                        colorScheme.onErrorContainer
                    else if (item.getCarryover().isZero())
                        colorScheme.onSurface
                    else
                        colorScheme.onTertiaryContainer,
                    fontSize = typography.bodyLarge.fontSize,
                    modifier = Modifier.weight(0.25f),
                    softWrap = false
                )
                Spacer(Modifier.weight(0.05f))
            }
            Divider(color = colorScheme.outline)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BudgetScreenLightThemePreview() {
    PBSM3Theme {
        BudgetScreen(selectedDate = LocalDate.now())
    }
}