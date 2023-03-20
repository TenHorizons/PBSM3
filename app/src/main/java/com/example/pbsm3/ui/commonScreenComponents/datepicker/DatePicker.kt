@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3.ui.commonScreenComponents.datepicker

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pbsm3.data.getFirstDayOfMonth
import com.example.pbsm3.Screen
import com.example.pbsm3.theme.PBSM3Theme
import com.maxkeppeker.sheets.core.icons.filled.ChevronLeft
import com.maxkeppeker.sheets.core.icons.filled.ChevronRight
import com.maxkeppeker.sheets.core.models.base.SheetState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeker.sheets.core.views.base.DialogBase
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG = "DatePicker"

@Composable
fun PBSDatePicker(
    modifier: Modifier = Modifier,
    viewModel: DatePickerViewModel = viewModel(),
    screen: Screen,
    onDateSelected: (LocalDate) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Column {
        when (screen) {
            Screen.Budget -> {
                MonthPicker(
                    withDialog = false,
                    onOk = { selectedDate ->
                        viewModel.setSelectedDate(getFirstDayOfMonth(selectedDate))
                        onDateSelected(selectedDate)
                    }, onCancel = viewModel::collapsePicker)
            }
            Screen.AddTransaction -> {
                DatePickerPlaceHolder(modifier, uiState, viewModel::expandPicker, screen)
                CalendarMode(
                    uiState = uiState,
                    onDateSelected = { selectedDate ->
                        viewModel.setSelectedDate(selectedDate)
                        onDateSelected(selectedDate)
                    },
                    onCloseRequest = { viewModel.collapsePicker() }
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun MonthPicker(
    modifier: Modifier = Modifier,
    viewModel: DatePickerViewModel = viewModel(),
    withDialog: Boolean,
    onOk: (LocalDate) -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedDate by remember { mutableStateOf(uiState.selectedDate) }

    val enterTransition =
        expandIn(expandFrom = Alignment.Center, clip = false) + fadeIn()
    val exitTransition =
        shrinkOut(shrinkTowards = Alignment.Center, clip = false) + fadeOut()
    val selectableContainerModifier =
        Modifier.clip(MaterialTheme.shapes.extraSmall)
    val selectableItemModifier = Modifier
        .padding(start = 8.dp, end = 4.dp)
        .padding(vertical = 4.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = if (withDialog) 0.dp else 16.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                AnimatedButton(
                    uiState = uiState,
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    onCLick = {
                        selectedDate = selectedDate.minusMonths(1)
                        if (!withDialog) onOk(selectedDate)
                        Log.d(TAG, "Left Clicked, date: $selectedDate")
                    },
                    imageVector = ChevronLeft)
                Spacer(
                    modifier =
                    if (withDialog) Modifier.weight(1f)
                    else Modifier.width(24.dp))
                Row(
                    modifier = selectableContainerModifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = selectableItemModifier,
                        text = selectedDate.format(
                            DateTimeFormatter.ofPattern("MMM yyyy")),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(
                    modifier =
                    if (withDialog) Modifier.weight(1f)
                    else Modifier.width(24.dp))
                AnimatedButton(
                    uiState = uiState,
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    onCLick = {
                        selectedDate = selectedDate.plusMonths(1)
                        if (!withDialog) onOk(selectedDate)
                        Log.d(TAG, "Right Clicked, date: $selectedDate")
                    },
                    imageVector = ChevronRight)
            }
            if (withDialog) {
                BottomButtons(
                    modifier = Modifier.align(Alignment.End),
                    onOk = { onOk(selectedDate) },
                    onCancel = onCancel
                )
            }
        }
    }
}

@Composable
private fun DatePickerPlaceHolder(
    modifier: Modifier = Modifier,
    uiState: DatePickerState,
    onCLick: () -> Unit,
    screen: Screen
) {
    Row(
        modifier = modifier.clickable { onCLick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = when (screen) {
                Screen.Budget -> uiState.selectedDate.format(
                    DateTimeFormatter.ofPattern("MMM yyyy"))
                Screen.AddTransaction -> uiState.selectedDate.toString()
                else -> ""
            },
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector =
                    if (uiState.pickerExpanded) Icons.Filled.ExpandLess
                    else Icons.Filled.ExpandMore,
                    contentDescription = "Select Date")
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red
            )
        )
    }
}

//copied from sheets-compose-dialog and modified to fit purpose
@Composable
private fun MonthMode(
    uiState: DatePickerState,
    onOk: (LocalDate) -> Unit,
    onCancel: () -> Unit
) {
    DialogBase(
        state = rememberSheetState(visible = uiState.pickerExpanded),
        properties = DialogProperties()
    ) {
        MonthPicker(withDialog = true, onOk = onOk, onCancel = onCancel)
    }
}

@Composable
private fun AnimatedButton(
    uiState: DatePickerState,
    enterTransition: EnterTransition,
    exitTransition: ExitTransition,
    onCLick: () -> Unit,
    imageVector: ImageVector
) {
    AnimatedVisibility(
        visible = uiState.previousEnabled,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column {
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.size(32.dp),
                onClick = onCLick
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = imageVector,
                    contentDescription = "Previous Month"
                )
            }
        }
    }
}

@Composable
private fun BottomButtons(modifier: Modifier = Modifier, onOk: () -> Unit, onCancel: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp),
            onClick = onCancel
        ) {
            Text("Cancel")
        }
        TextButton(
            modifier = Modifier.wrapContentWidth(),
            onClick = onOk
        ) {
            Text("Ok")
        }
    }
}

@Composable
private fun CalendarMode(
    uiState: DatePickerState,
    onDateSelected: (LocalDate) -> Unit,
    onCloseRequest: (SheetState.() -> Unit)?
) {
    CalendarDialog(
        state = rememberSheetState(
            visible = uiState.pickerExpanded,
            onCloseRequest = onCloseRequest),
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Date { newDate ->
            onDateSelected(newDate)
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PBSDatePickerMonthModePreview() {
    PBSM3Theme {
        val viewModel: DatePickerViewModel = viewModel()
        viewModel.expandPicker()
        PBSDatePicker(viewModel = viewModel, screen = Screen.Budget, onDateSelected = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PBSDatePickerCalendarModePreview() {
    PBSM3Theme {
        val viewModel: DatePickerViewModel = viewModel()
        viewModel.expandPicker()
        PBSDatePicker(viewModel = viewModel, screen = Screen.AddTransaction, onDateSelected = {})
    }
}