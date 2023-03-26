package com.example.pbsm3.ui.screens.addAccount

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.theme.PBSM3Theme

@Composable
fun AddAccountScreen(
    onBackPressed:()->Unit ={}
) {
    BackHandler(onBack = onBackPressed)
    //TODO: not yet implemented
}


@Preview
@Composable
fun AddAccountScreenPreview() {
    PBSM3Theme {
        AddAccountScreen()
    }
}