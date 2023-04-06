package com.example.pbsm3.ui.screens.signup

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.PasswordField
import com.example.pbsm3.ui.commonScreenComponents.UsernameField

private const val TAG ="SignUpScreen"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {},
    viewModel: SignUpScreenViewModel = hiltViewModel(),
    onBackPressed:()->Unit={}
) {
    BackHandler(onBack = onBackPressed)
    val uiState by viewModel.uiState

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp)

        UsernameField(uiState.username, viewModel::onUsernameChange, textFieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, textFieldModifier)

        Button(
            onClick = {
                keyboardController?.hide()
                viewModel.onSignUpClick(onComplete = onComplete)
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
            if(viewModel.signUpClickInProgress.value){
                CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
            }else {
                Text(text = "Sign Up")
            }
        }
        if(viewModel.usernameExist.value){
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer)
            ){
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Username already exists!"
                )
            }
            viewModel.onUsernameExists()
        }
    }


}

@Preview
@Composable
fun SignUpScreenPreview() {
    PBSM3Theme {
        SignUpScreen()
    }
}