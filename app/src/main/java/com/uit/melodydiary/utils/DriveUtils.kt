//package com.uit.melodydiary.utils
//
//import android.content.Context
//import android.widget.Toast
//import androidx.compose.runtime.rememberCoroutineScope
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
//import com.google.api.client.http.InputStreamContent
//import com.google.api.client.http.javanet.NetHttpTransport
//import com.google.api.client.json.gson.GsonFactory
//import com.google.api.services.drive.Drive
//import com.google.api.services.drive.DriveScopes
//import com.uit.melodydiary.R
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.io.IOException
//
//
//fun uploadFileToDrive(account: GoogleSignInAccount, context: Context) {
//    val coroutineScope = rememberCoroutineScope()
//    coroutineScope.launch {
//        try {
//            val credential = GoogleAccountCredential.usingOAuth2(
//                context,
//                listOf(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_APPDATA)
//            )
//            credential.selectedAccount = account.account
//
//            val driveService = Drive.Builder(
//                NetHttpTransport(),
//                GsonFactory(),
//                credential
//            )
//                .setApplicationName(context.getString(R.string.app_name))
//                .build()
//
//            val fileMetadata = File()
//            fileMetadata.name = "backup.txt"
//            val content = "This is a backup file."
//            val fileContent = content.toByteArray().inputStream()
//
//            val mediaContent = InputStreamContent("text/plain", fileContent)
//            val file = driveService.files().create(fileMetadata, mediaContent)
//                .setFields("id")
//                .execute()
//
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "File uploaded successfully: ${file.id}", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: IOException) {
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "Failed to upload file: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}