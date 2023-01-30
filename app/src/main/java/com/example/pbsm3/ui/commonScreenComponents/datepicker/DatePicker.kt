@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3.ui.commonScreenComponents.datepicker

import android.util.Log
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
import com.example.pbsm3.ui.theme.PBSM3Theme
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pbsm3.data.shortMonthStrings

private const val TAG = "DatePicker"

@Composable
fun PBSDatePicker(
    viewModel: DatePickerViewModel = viewModel(),
    onClick: (Date) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Column {
        DatePickerPlaceHolder(viewModel)
        if (true) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = viewModel::collapsePicker
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

@Composable
private fun DatePickerPlaceHolder(viewModel: DatePickerViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Row(
        modifier = Modifier.clickable{ viewModel.expandOrCollapsePicker() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(value = "", onValueChange = {}, readOnly = true)
        Icon(
            imageVector = Icons.Filled.ExpandMore, contentDescription =
            "Select Date")
    }
}

//copied from sheets-compose-dialog
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun MonthYearPicker(viewModel: DatePickerViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val chevronAVD = AnimatedImageVector.animatedVectorResource(
        R.drawable.avd_chevron_down_up)
    val enterTransition =
        expandIn(expandFrom = Alignment.Center, clip = false) + fadeIn()
    val exitTransition =
        shrinkOut(shrinkTowards = Alignment.Center, clip = false) + fadeOut()
    val selectableContainerModifier =
        Modifier.clip(MaterialTheme.shapes.extraSmall)
    val selectableItemModifier = Modifier
        .padding(start = 8.dp, end = 4.dp)
        .padding(vertical = 4.dp)

    Log.d(TAG,"Selected Date: ${uiState.selectedDate}")

    Box(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = uiState.navigationEnabled && uiState.previousEnabled,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column(Modifier.align(Alignment.CenterStart)) {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .size(32.dp),
                    onClick = { viewModel.moveToPreviousMonth() }
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
                    .clickable { viewModel.displayMonthList() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = selectableItemModifier,
                    text = shortMonthStrings[uiState.displayedMonthIndex],
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = rememberAnimatedVectorPainter(
                        animatedImageVector =  chevronAVD,
                        atEnd = uiState.displayMode == CalendarDisplayMode.MONTH),
                    contentDescription = "Select Month",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = selectableContainerModifier
                    .clickable { viewModel.displayYearList() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = selectableItemModifier,
                    text = uiState.displayedYear.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = rememberAnimatedVectorPainter(
                        chevronAVD,
                        uiState.displayMode == CalendarDisplayMode.YEAR),
                    contentDescription = "Select Year",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterEnd),
            visible = uiState.navigationEnabled && uiState.nextEnabled,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column(Modifier.align(Alignment.CenterEnd)) {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.size(32.dp),
                    onClick = { viewModel.moveToNextMonth() }
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

@Preview(showBackground = true)
@Composable
fun PBSDatePickerPreview() {
    PBSM3Theme {
        PBSDatePicker(onClick = { })
    }
}

@Preview(showBackground = true)
@Composable
fun MonthYearPickerPreview() {
    PBSM3Theme {
        MonthYearPicker(viewModel())
    }
}
