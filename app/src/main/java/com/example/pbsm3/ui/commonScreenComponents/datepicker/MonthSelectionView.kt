/*
 *  Copyright (C) 2022. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.example.pbsm3.ui.commonScreenComponents.datepicker

import androidx.compose.foundation.lazy.grid.LazyGridScope
import com.example.pbsm3.data.shortMonthStrings
import java.time.LocalDate
import java.time.Month

/**
 * The view that displays all relevant year information.
 * @param monthRange The range of months.
 * @param selectedMonthIndex The month that is currently selected.
 * @param onMonthClick The listener that is invoked when a month is selected.
 */
internal fun LazyGridScope.setupMonthSelectionView(
    selectedMonthIndex: Int,
    onMonthClick: (Int?) -> Unit,
) {
    items(4) {
        MonthItemComponent(month = 0, onMonthClick = onMonthClick)
        MonthItemComponent(month = 1, onMonthClick = onMonthClick)
        MonthItemComponent(month = 2, onMonthClick = onMonthClick)
        MonthItemComponent(month = 3, onMonthClick = onMonthClick)
    }
    items(4) {
        MonthItemComponent(month = 4, onMonthClick = onMonthClick)
        MonthItemComponent(month = 5, onMonthClick = onMonthClick)
        MonthItemComponent(month = 6, onMonthClick = onMonthClick)
        MonthItemComponent(month = 7, onMonthClick = onMonthClick)
    }
    items(4) {
        MonthItemComponent(month = 8, onMonthClick = onMonthClick)
        MonthItemComponent(month = 9, onMonthClick = onMonthClick)
        MonthItemComponent(month = 10, onMonthClick = onMonthClick)
        MonthItemComponent(month = 11, onMonthClick = onMonthClick)
    }
}

