package com.example.pbsm3.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.ui.theme.PBSM3Theme

@Composable
fun LoginScreen(onVerified:(String)->Unit) {

}

@Preview
@Composable
fun LoginScreenPreview() {
    PBSM3Theme {
        LoginScreen(onVerified = {})
    }
}