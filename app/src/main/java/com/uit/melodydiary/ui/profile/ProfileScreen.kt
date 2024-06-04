package com.uit.melodydiary.ui.profile


import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uit.melodydiary.R
import com.uit.melodydiary.ui.theme.MelodyDiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Setting",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
            )
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValue),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BackupWrapper(
                    onBackUpButtonClick = {
                        Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            item {
                AppSettingWrapper()
            }
            item {
                AppInformationWrapper()
            }
            item {
                Text(
                    text = stringResource(R.string.title_footer),
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BackupWrapper(
    modifier: Modifier = Modifier,
    onBackUpButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.title_back_up),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.title_back_up_description),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onBackUpButtonClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.alpha(0f)
            ) {
                Text(
                    text = stringResource(R.string.btn_back_up),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AppSettingWrapper(
    modifier: Modifier = Modifier,

) {
    val context = LocalContext.current
    Column(
        modifier = modifier.background(Color.White, shape = RoundedCornerShape(20.dp))

    ) {
        SettingItem(
            icon = R.drawable.ic_theme,
            title = stringResource(R.string.title_app_theme),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_notification,
            title = stringResource(R.string.title_notification),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_password,
            title = stringResource(R.string.title_passcode),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_fun,
            title = stringResource(R.string.title_mood_gallery),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_language,
            title = stringResource(R.string.title_language),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_datetime,
            title = stringResource(R.string.title_datetime),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun AppInformationWrapper(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.background(Color.White, shape = RoundedCornerShape(20.dp))
    ) {
        SettingItem(
            icon = R.drawable.ic_faq,
            title = stringResource(R.string.title_faq),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_terms_of_use,
            title = stringResource(R.string.title_terms_of_use),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_privac_policy,
            title = stringResource(R.string.title_privacy_policy),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_feedback,
            title = stringResource(R.string.title_feedback),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
        SettingItem(
            icon = R.drawable.ic_rate,
            title = stringResource(R.string.title_rate_us),
            onItemClick = {
                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                onItemClick()
            }
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Image(
            painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    MelodyDiaryTheme {
        ProfileScreen()
    }
}