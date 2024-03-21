package com.example.melodydiary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.melodydiary.ui.diary.DiaryScreen
import com.example.melodydiary.ui.theme.MelodyDiaryTheme

@Composable
fun MelodyDiaryApp() {
    MelodyDiaryTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    BottomNavigationWithFab(
                        onFabClick = {}
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
            ) { paddingValues ->
                DiaryScreen(Modifier.padding(paddingValues))
            }
        }
    }
}

@Composable
fun BottomNavigationWithFab(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = modifier
    ) {
        NavigationBarItem(
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_diary)
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_diary),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = true,
            onClick = {

            }
        )
        NavigationBarItem(
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_music)
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.library_music),
                    contentDescription = null
                )
            },
            selected = false,
            onClick = {

            }
        )

        DiamondFab()
        NavigationBarItem(
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_report)
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.bar_chart_24px),
                    contentDescription = null
                )
            },
            selected = false,
            onClick = {

            }
        )
        NavigationBarItem(
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_profile)
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.person),
                    contentDescription = null
                )
            },
            selected = false,
            onClick = {

            }
        )
    }
}
@Composable
fun DiamondFab(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.height(100.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        FloatingActionButton(
            onClick = { /* Handle FAB click */ },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = 45f
                },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = -45f
                    }

            )
        }
    }
}

@Preview
@Composable
fun DiamondFabPreview() {
    DiamondFab(Modifier.padding(bottom = 30.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun PreviewNavBar(
) {
    MelodyDiaryTheme {
        BottomNavigationWithFab(Modifier.padding(16.dp), {})
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    MelodyDiaryTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DiaryScreen()
        }
    }
}
