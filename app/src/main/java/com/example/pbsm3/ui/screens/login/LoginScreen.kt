package com.example.pbsm3.ui.screens.login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.EmailField
import com.example.pbsm3.ui.commonScreenComponents.PasswordField

private const val TAG ="Login"

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateAndPopUpInclusive: (String, String) -> Unit,
    viewModel: LoginScreenViewModel  = hiltViewModel(),
    onBackPressed:()->Unit={}
) {
    BackHandler(onBack = onBackPressed)
    val uiState by viewModel.uiState

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp)

        EmailField(uiState.email, viewModel::onEmailChange, textFieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, textFieldModifier)

        Button(
            onClick = { viewModel.onSignInClick(navigateAndPopUpInclusive) },
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

        //TODO Register Function and page

        TextButton(
            onClick = { viewModel.onForgotPasswordClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 0.dp)
        ) {
            Text(text = "Forgot password? Click to get recovery email")
        }
    }


}

fun Context.getActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> throw IllegalStateException("no activity")
}

@Preview
@Composable
fun LoginScreenPreview() {
    PBSM3Theme {
        LoginScreen(navigateAndPopUpInclusive = {_,_->})
    }
}