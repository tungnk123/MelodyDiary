package com.uit.melodydiary.ui.profile

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.InputStreamContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.uit.melodydiary.BuildConfig
import com.uit.melodydiary.R
import com.uit.melodydiary.utils.AuthResultContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleAccount = remember { mutableStateOf<GoogleSignInAccount?>(null) }

    val sharedPref = context.getSharedPreferences(
        "user_pref",
        Context.MODE_PRIVATE
    )
    var savedEmail = remember {
        mutableStateOf(
            sharedPref.getString(
                "email",
                "example@gmail.com"
            )
        )
    }


    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.WEB_CLIENT_ID)
        .requestScopes(
            Scope(Scopes.DRIVE_APPFOLDER),
            Scope(Scopes.DRIVE_FILE)
        )
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(
        context,
        gso
    )
    val signInRequestCode = 1

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract(googleSignInClient)) {
            handleSignInResult(
                it,
                googleAccount,
                context as Activity
            )
        }

    LaunchedEffect(googleAccount.value) {
        savedEmail.value = sharedPref.getString(
            "email",
            "example@gmail.com"
        )
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                googleAccount = savedEmail.value!!,
                onGoogleAccountClick = {
                    authResultLauncher.launch(signInRequestCode)
                },
                onSynchorizeClick = {
//                    googleSignInClient.signOut()

                    scope.launch(Dispatchers.IO) {
                        val fileMetadata = com.google.api.services.drive.model.File()
                        fileMetadata.name = "TextFile.txt" // Set the name of the file

                        // Convert text to InputStream
                        val text = "test drive"
                        val inputStream = ByteArrayInputStream(text.toByteArray())

                        // Create a file content instance
                        val mediaContent = InputStreamContent(
                            "text/plain",
                            inputStream
                        )

                        // Get the Drive service instance
                        val driveService = getDriveService(
                            googleAccount.value!!,
                            context
                        )
                        Log.d(
                            "DriveUpload",
                            "drive service: ${driveService!!.applicationName}"
                        )
                        try {

                            val driveFile = driveService.files()
                                .create(
                                    fileMetadata,
                                    mediaContent
                                )
                                .setFields("id")
                                .execute()
                            if (driveFile != null) {
                                Log.d(
                                    "DriveUpload",
                                    "File uploaded: ${driveFile.name} (${driveFile.id})"
                                )
//                                Toast.makeText(context, "File uploaded successfully", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Log.e(
                                    "DriveUpload",
                                    "Null Drive file received after upload"
                                )
//                                Toast.makeText(context, "File upload failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: Exception) {
                            Log.e(
                                "DriveUpload",
                                "Error uploading file to Drive: ${e.message}",
                                e
                            )
//                            Toast.makeText(context, "File upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            )
            BackUpWrapper(
                onExportClick = {
                    Toast.makeText(
                        context,
                        "Feature is under construction",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                },
                onImportClick = {
                    Toast.makeText(
                        context,
                        "Feature is under construction",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            )
        }
    }
}

private fun getDriveService(
    account: GoogleSignInAccount,
    context: Context,
): Drive? {
    Log.d(
        "DriveService",
        "Attempting to get last signed-in account..."
    )
    val googleAccount = account
    val credential = GoogleAccountCredential.usingOAuth2(
        context,
        listOf(
            DriveScopes.DRIVE_FILE,
            DriveScopes.DRIVE_APPDATA
        )
    )
    credential.selectedAccount = googleAccount.account!!
    Log.d(
        "DriveService",
        "Building Drive service..."
    )
    val drive = Drive.Builder(
        NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        credential
    )
        .setApplicationName(context.getString(R.string.app_name))
        .build()
    Log.d(
        "DriveService",
        "Drive service built successfully. ${drive.About()}"
    )
    return drive
}


fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>?,
    googleAccount: MutableState<GoogleSignInAccount?>,
    context: Activity,
) {
    try {
        val account = completedTask?.getResult(ApiException::class.java)
        val email = account?.email ?: "example@gmail.com"
        if (account != null) {
            googleAccount.value = account
        }

        val sharedPref = context.getSharedPreferences(
            "user_pref",
            Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString(
                "email",
                email
            )
            apply()
        }
        Toast.makeText(
            context,
            "Login thành công!, email: ${account?.email}",
            Toast.LENGTH_LONG
        )
            .show()
        Log.d(
            "GoogleSignIn",
            "Account details: ${account?.email}, ${account?.idToken}"
        )
    }
    catch (e: ApiException) {
        Toast.makeText(
            context,
            "Sign-in failed: ${e.message}",
            Toast.LENGTH_SHORT
        )
            .show()
        Log.e(
            "GoogleSignIn",
            "Sign-in failed",
            e
        )
    }
}

@Composable
fun SyncWrapper(
    modifier: Modifier = Modifier,
    googleAccount: String,
    onGoogleAccountClick: () -> Unit,
    onSynchorizeClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
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
    onImportClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
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
    onItemClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clickable {
                onItemClick()
            }
            .fillMaxWidth()
            .padding(
                vertical = 15.dp,
                horizontal = 10.dp
            ),
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
