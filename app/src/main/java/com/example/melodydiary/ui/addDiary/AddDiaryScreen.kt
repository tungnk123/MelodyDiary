package com.example.melodydiary.ui.addDiary

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.melodydiary.R
import com.example.melodydiary.ui.theme.MelodyDiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDiaryScreen(
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(end = 20.dp),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.title_viet_nhat_ky),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            onClick = {}
                        ) {
                            Text(
                                text = "Lưu",
                                color = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValue ->
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValue)
        ) {
            DateDetailInDiaryWithSelection(
                date = "3/10",
                time = "12:30",
                thu = "Thu 3",
                R.drawable.ic_face
            )
            BorderlessTextField(
                value = "",
                placeholder = "Tiêu đề",
                onValueChange = {}
            )

        }
    }
}


@Composable
fun BorderlessTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.titleLarge
            )
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.titleLarge,
        modifier = modifier.fillMaxWidth().border(
            width = 0.dp,
            color = Color.Transparent,
            shape = RoundedCornerShape(0.dp)
        )
    )
}

@Composable
fun DiaryTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier.fillMaxWidth().border(
            width = 0.dp,
            color = Color.Transparent,
            shape = RoundedCornerShape(0.dp)
        )
    )
}

@Composable
fun DateDetailInDiaryWithSelection(
    date: String,
    time: String,
    thu: String,
    @DrawableRes statusLogoRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier.width(2.dp).height(32.dp).background(Color.Black)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(
                text = time
            )
            Text(
                text = thu
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(statusLogoRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMusicScreen() {
    MelodyDiaryTheme {
        AddDiaryScreen()
    }
}