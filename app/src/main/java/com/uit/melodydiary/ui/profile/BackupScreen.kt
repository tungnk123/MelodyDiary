package com.uit.melodydiary.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.uit.melodydiary.R
import com.uit.melodydiary.data.WEB_CLIENT_ID
import com.uit.melodydiary.utils.AuthResultContract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val googleAccount = remember { mutableStateOf("") }

    val sharedPref = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    val savedEmail = sharedPref.getString("email", null)
    if (savedEmail != null) {
        googleAccount.value = savedEmail
    } else {
        googleAccount.value = "example@gmail.com"
    }
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(WEB_CLIENT_ID)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    val signInRequestCode = 1

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract(googleSignInClient)) {
            handleSignInResult(it, googleAccount, context as Activity)
        }


    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.title_backup_screen),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

            )
        },
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
        ) {
            SyncWrapper(
                googleAccount = googleAccount.value,
                onGoogleAccountClick = {
                    authResultLauncher.launch(signInRequestCode)
                },
                onSynchorizeClick = {
                    googleSignInClient.signOut()
                    Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
                }
            )
            BackUpWrapper(
                onExportClick = {
                    Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
                },
                onImportClick = {
                    Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>?,
    googleAccount: MutableState<String>,
    context: Activity
) {
    try {
        val account = completedTask?.getResult(ApiException::class.java)
        val email = account?.email ?: "example@gmail.com"
        googleAccount.value = email

        val sharedPref = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("email", email)
            apply()
        }
        Toast.makeText(context, "Login thành công!, email: ${account?.email}", Toast.LENGTH_LONG).show()
        Log.d("GoogleSignIn", "Account details: ${account?.email}, ${account?.idToken}")
    } catch (e: ApiException) {
        Toast.makeText(context, "Sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        Log.e("GoogleSignIn", "Sign-in failed", e)
    }
}

@Composable
fun SyncWrapper(
    modifier: Modifier = Modifier,
    googleAccount: String,
    onGoogleAccountClick: () -> Unit,
    onSynchorizeClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.title_sync),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.width(10.dp))
        BackupItem(
            logo = R.drawable.ic_user,
            title = stringResource(R.string.title_google_account),
            subtitle = googleAccount,
            onItemClick = onGoogleAccountClick
        )

        BackupItem(
            logo = R.drawable.ic_sync,
            title = stringResource(R.string.title_sync),
            subtitle = "",
            onItemClick = onSynchorizeClick
        )
    }
}

@Composable
fun BackUpWrapper(
    modifier: Modifier = Modifier,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.title_backup),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.width(10.dp))
        BackupItem(
            logo = R.drawable.ic_duplicate,
            title = stringResource(R.string.title_backup),
            subtitle = stringResource(R.string.title_backup),
            onItemClick = onExportClick
        )

        BackupItem(
            logo = R.drawable.ic_import,
            title = stringResource(R.string.title_import),
            subtitle = "your_path_to_import",
            onItemClick = onImportClick
        )
    }
}

@Composable
fun BackupItem(
    modifier: Modifier = Modifier,
    @DrawableRes logo: Int,
    title: String,
    subtitle: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                onItemClick()
            }
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(logo),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            if (subtitle != "") {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
