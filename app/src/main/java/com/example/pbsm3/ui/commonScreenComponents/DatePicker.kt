@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3.ui.commonScreenComponents

import androidx.compose.animation.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.pbsm3.R
import com.example.pbsm3.data.monthStrings
import com.example.pbsm3.ui.commonScreenComponents.utils.CalendarDisplayMode
import com.example.pbsm3.ui.theme.PBSM3Theme
import com.maxkeppeker.sheets.core.icons.filled.ChevronLeft
import com.maxkeppeker.sheets.core.icons.filled.ChevronRight
import java.text.DateFormatSymbols
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PBSDatePicker(onClick: (Date) -> Unit) {
    var expanded by remember { mutableStateOf(true) }
    Column {
        Row(
            modifier = Modifier.clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically) {
            TextField(value = "", onValueChange = {}, readOnly = true)
            Icon(
                imageVector = Icons.Filled.ExpandMore, contentDescription =
                "Select Date")
        }
        if (true) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { expanded = false }
            ) {
                Box(
                    Modifier
                        .wrapContentWidth()
                        .widthIn(max = 300.dp)
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp,
                    ) {

                    }
                }
            }
        }
    }
}

//copied from sheets-compose-dialog
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun MonthYearPicker() {
    val enterTransition =
        expandIn(expandFrom = Alignment.Center, clip = false) + fadeIn()
    val exitTransition =
        shrinkOut(shrinkTowards = Alignment.Center, clip = false) + fadeOut()

    val chevronAVD = AnimatedImageVector.animatedVectorResource(
        R.drawable.avd_chevron_down_up)
    var monthExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }
    var mode: CalendarDisplayMode = CalendarDisplayMode.NONE
    LaunchedEffect(mode) {
        when (mode) {
            CalendarDisplayMode.MONTH -> yearExpanded = false
            CalendarDisplayMode.YEAR -> monthExpanded = false
            else -> {
                yearExpanded = false
                monthExpanded = false
            }
        }
    }


    var displayedMonthIndex: Int by remember { mutableStateOf(0) }
    var displayedYear by remember { mutableStateOf(2023) }

    var navigationEnabled by remember { mutableStateOf(true) }
    var previousEnabled by remember { mutableStateOf(true) }
    var nextEnabled by remember { mutableStateOf(true) }


    val selectableContainerModifier =
        Modifier.clip(MaterialTheme.shapes.extraSmall)
    val selectableItemModifier = Modifier
        .padding(start = 8.dp, end = 4.dp)
        .padding(vertical = 4.dp)

    Box(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = navigationEnabled && previousEnabled,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column(Modifier.align(Alignment.CenterStart)) {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .size(32.dp),
                    onClick = /*onPrev*/{}
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ChevronLeft,
                        contentDescription = "Previous Month"
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = selectableContainerModifier
                    .clickable {
                        monthExpanded = !monthExpanded
//                        onMonthClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = selectableItemModifier,
                    text = monthStrings[displayedMonthIndex],
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = rememberAnimatedVectorPainter(
                        chevronAVD, monthExpanded),
                    contentDescription = "Select Month",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = selectableContainerModifier
                    .clickable {
                        yearExpanded = !yearExpanded
//                        onYearClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = selectableItemModifier,
                    text = displayedYear.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = rememberAnimatedVectorPainter(
                        chevronAVD, yearExpanded),
                    contentDescription = "Select Year",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterEnd),
            visible = navigationEnabled && nextEnabled,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column(Modifier.align(Alignment.CenterEnd)) {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.size(32.dp),
                    onClick = /*onNext*/{}
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ChevronRight,
                        contentDescription = "Next Month"
                    )
                }
            }
        }
    }
}

private val validYearRange: IntRange = IntRange(
    start = Calendar.getInstance().get(Calendar.YEAR).minus(10),
    endInclusive = Calendar.getInstance().get(Calendar.YEAR).plus(11)
)

private fun validateMonthRange(month: Int): Int {
    val validIndexes: IntRange = monthStrings.indices
    if (validIndexes.contains(month)) return month
    else {
        if (month < validIndexes.first) {
            if()
        } else {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PBSDatePickerPreview() {
    PBSM3Theme {
        PBSDatePicker(onClick = { })
    }
}

@Preview
@Composable
fun MonthYearPickerPreview() {
    PBSM3Theme {
        MonthYearPicker()
    }
}
