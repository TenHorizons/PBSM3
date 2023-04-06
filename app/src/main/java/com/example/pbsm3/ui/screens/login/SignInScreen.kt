package com.example.pbsm3.ui.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.PasswordField
import com.example.pbsm3.ui.commonScreenComponents.UsernameField

private const val TAG = "Login"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit = {},
    onComplete: () -> Unit = {},
    viewModel: SignInScreenViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {}
) {
    BackHandler(onBack = onBackPressed)
    val uiState by viewModel.uiState

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp)

        UsernameField(uiState.username, viewModel::onUsernameChange, textFieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, textFieldModifier)

        Button(
            onClick = {
                keyboardController?.hide()
                viewModel.onSignInClick(onComplete = onComplete)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Sign in", fontSize = 16.sp)
        }

        TextButton(
            onClick = {
                keyboardController?.hide()
                onSignUpClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 0.dp)
        ) {
            if (viewModel.signInClickInProgress.value) {
                CircularProgressIndicator()
            } else {
                Text(text = "New User? Click to Sign Up")
            }
        }
        if (viewModel.isPasswordWrong.value) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Invalid Username or Password."
                )
            }
            viewModel.onPasswordWrong()
        }
    }


}

@Preview
@Composable
fun LoginScreenPreview() {
    PBSM3Theme {
        SignInScreen()
    }
}