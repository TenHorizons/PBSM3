package com.example.pbsm3.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.IntentSender
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.example.pbsm3.R
import com.example.pbsm3.ui.screens.viewmodel.LoginScreenState
import com.example.pbsm3.ui.screens.viewmodel.LoginScreenViewModel
import com.example.pbsm3.ui.theme.PBSM3Theme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient

private lateinit var oneTapClient: SignInClient
private lateinit var signInRequest: BeginSignInRequest

private const val TAG ="Login"
@Composable
fun LoginScreen(
    onVerified:(String)->Unit,
    viewModel: LoginScreenViewModel
) {
    val uiState:LoginScreenState by
    val activity:Activity = LocalContext.current.getActivity()
    oneTapClient = Identity.getSignInClient(activity)
    signInRequest = BeginSignInRequest.builder()
        .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
            .setSupported(true)
            .build())
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId("348605648969-nm94pk9v875onf0v7a0kp986vsrd4lbv.apps.googleusercontent.com")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build())
        // Automatically sign in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener(activity) { result ->
            try {
                startIntentSenderForResult(
                    activity,
                    result.pendingIntent.intentSender, REQ_ONE_TAP,
                    null, 0, 0, 0, null)
            } catch (e: IntentSender.SendIntentException) {
                Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
            }
        }
        .addOnFailureListener(activity) { e ->
            // No saved credentials found. Launch the One Tap sign-up flow, or
            // do nothing and continue presenting the signed-out UI.
            e.localizedMessage?.let { Log.d(TAG, it) }
        }
}

fun Context.getActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> throw IllegalStateException("no activity")
}

/*fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}*/

@Preview
@Composable
fun LoginScreenPreview() {
    PBSM3Theme {
        LoginScreen(onVerified = {})
    }
}