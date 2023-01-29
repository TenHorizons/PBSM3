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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pbsm3.data.shortMonthStrings
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

/**
 * The item component of the month selection view.
 * @param month The month that this item represents.
 * @param thisMonth The current month.
 * @param selected If the month is selected.
 * @param onMonthClick The listener that is invoked when a year is selected.
 */
@Composable
internal fun MonthItemComponent(
    month: Int,
    thisMonth: Boolean = false,
    selected: Boolean = false,
    onMonthClick: (Int?) -> Unit
) {
    val textStyle =
        when {
            selected -> MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.onPrimary)
            thisMonth -> MaterialTheme.typography.titleSmall.copy(MaterialTheme.colorScheme.primary)
            else -> MaterialTheme.typography.bodyMedium
        }

    val baseModifier = Modifier
        .wrapContentWidth()
        .padding(4.dp)

    val normalModifier = baseModifier
        .clip(MaterialTheme.shapes.small)
        .clickable { onMonthClick(month) }

    val selectedModifier = normalModifier
        .background(MaterialTheme.colorScheme.primary)

    Column(
        modifier = when {
            /*month == null -> baseModifier*/
            selected -> selectedModifier
            else -> normalModifier
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(vertical = 8.dp),
            text = shortMonthStrings[month],
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}