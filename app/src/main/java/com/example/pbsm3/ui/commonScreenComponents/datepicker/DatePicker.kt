@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3.ui.commonScreenComponents.datepicker

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import com.example.pbsm3.R
import com.example.pbsm3.ui.theme.PBSM3Theme
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pbsm3.data.shortMonthStrings
import com.example.pbsm3.ui.navhost.Screen
import com.maxkeppeker.sheets.core.models.base.SheetState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

private const val TAG = "DatePicker"

@Composable
fun PBSDatePicker(
    viewModel: DatePickerViewModel = viewModel(),
    screen: Screen,
) {
    Column {
        DatePickerPlaceHolder(viewModel, screen)
        if (true) {
            when (screen) {
                Screen.Budget -> {
                    MonthYearPicker(viewModel = viewModel)
                }
                Screen.Transaction -> {
                    CalendarMode(viewModel = viewModel)
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun DatePickerPlaceHolder(
    viewModel: DatePickerViewModel,
    screen: Screen
) {
    val uiState by viewModel.uiState.collectAsState()
    Row(
        modifier = Modifier.clickable { viewModel.expandOrCollapsePicker() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = when (screen) {
                Screen.Budget -> uiState.selectedDate.format(
                    DateTimeFormatter.ofPattern("MMM yyyy"))
                Screen.Transaction -> uiState.selectedDate.toString()
                else -> ""
            },
            onValueChange = {},
            readOnly = true)
        Icon(
            imageVector =
            if(uiState.pickerExpanded) Icons.Filled.ExpandLess
            else Icons.Filled.ExpandMore,
            contentDescription = "Select Date")
    }
}

//copied from sheets-compose-dialog
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun MonthYearPicker(viewModel: DatePickerViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val enterTransition =
        expandIn(expandFrom = Alignment.Center, clip = false) + fadeIn()
    val exitTransition =
        shrinkOut(shrinkTowards = Alignment.Center, clip = false) + fadeOut()
    val selectableContainerModifier =
        Modifier.clip(MaterialTheme.shapes.extraSmall)
    val selectableItemModifier = Modifier
        .padding(start = 8.dp, end = 4.dp)
        .padding(vertical = 4.dp)

    Log.d(TAG, "Selected Date: ${uiState.selectedDate}")

    Box(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = uiState.previousEnabled,
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
                    text = uiState.selectedDate.month.getDisplayName(
                        TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = selectableContainerModifier
                    .clickable { viewModel.displayYearList() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = selectableItemModifier,
                    text = uiState.selectedDate.year.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterEnd),
            visible = uiState.nextEnabled,
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

@Composable
private fun CalendarMode(
    viewModel: DatePickerViewModel
) {
    var selectedDate: LocalDate by remember { mutableStateOf(LocalDate.now()) }
    CalendarDialog(
        state = rememberSheetState(
            visible = true,
            onCloseRequest = { viewModel.setSelectedDate(selectedDate) }),
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
            disabledDates = null
        ),
        selection = CalendarSelection.Date { newDate ->
            selectedDate = newDate
        },
    )
}

@Preview(showBackground = true)
@Composable
fun PBSDatePickerMonthYearPreview() {
    PBSM3Theme {
        PBSDatePicker(onClick = { }, screen = Screen.Transaction)
    }
}

@Preview(showBackground = true)
@Composable
fun PBSDatePickerCalendarPreview() {
    PBSM3Theme {
        PBSDatePicker(onClick = { }, screen = Screen.Budget)
    }
}

@Preview(showBackground = true)
@Composable
fun MonthYearPickerPreview() {
    PBSM3Theme {
        MonthYearPicker(viewModel())
    }
}
