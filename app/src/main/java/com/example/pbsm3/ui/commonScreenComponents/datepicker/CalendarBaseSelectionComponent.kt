package com.example.pbsm3.ui.commonScreenComponents.datepicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * The main component for the selection of the use-case as well as the selection of month and year within the dialog.
 * @param modifier The modifier that is applied to this component.
 * @param yearListState The state of the year list selection view.
 * @param cells The amount of cells / columns that are used for the calendar grid view.
 * @param mode The display mode of the dialog.
 * @param onCalendarView The content that will be displayed if the [CalendarDisplayMode] is in [CalendarDisplayMode.CALENDAR].
 * @param onMonthView The content that will be displayed if the [CalendarDisplayMode] is in [CalendarDisplayMode.MONTH].
 * @param onYearView The content that will be displayed if the [CalendarDisplayMode] is in [CalendarDisplayMode.YEAR].
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarBaseSelectionComponent(
    modifier: Modifier,
    yearListState: LazyListState,
    cells: Int,
    mode: CalendarDisplayMode,
    onMonthView: LazyGridScope.() -> Unit,
    onYearView: LazyListScope.() -> Unit
) {

    val baseModifier = modifier
        .fillMaxWidth()
        .padding(top = 16.dp)

    val gridYearModifier = baseModifier
        .graphicsLayer { alpha = 0.99F }
        .drawWithContent {
            val colorStops = arrayOf(
                0.0f to Color.Transparent,
                0.25f to Color.Black,
                0.75f to Color.Black,
                1.0f to Color.Transparent
            )
            drawContent()
            drawRect(
                brush = Brush.horizontalGradient(*colorStops),
                blendMode = BlendMode.DstIn
            )
        }

    //https://github.com/chrisbanes/snapper
    val behavior = rememberSnapFlingBehavior(lazyListState = yearListState)

    when (mode) {
        CalendarDisplayMode.YEAR -> {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Year",
                    style = MaterialTheme.typography.titleMedium,
                )
                LazyRow(
                    modifier = gridYearModifier,
                    state = yearListState,
                    flingBehavior = behavior,
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    onYearView()
                }
            }
        }
        CalendarDisplayMode.MONTH -> {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Month",
                    style = MaterialTheme.typography.titleMedium,
                )
                LazyVerticalGrid(
                    modifier = baseModifier,
                    columns = GridCells.Fixed(cells),
                ) {
                    onMonthView()
                }
            }
        }
        else -> {}
    }
}