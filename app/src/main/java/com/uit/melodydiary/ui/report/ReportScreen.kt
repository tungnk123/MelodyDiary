package com.uit.melodydiary.ui.report


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uit.melodydiary.ui.diary.DiaryViewModel
import com.uit.melodydiary.ui.theme.MelodyDiaryTheme
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    diaryViewModel: DiaryViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        diaryViewModel.getDiaryFromDatabase()
    }
    val diaryList = diaryViewModel.diaryList.collectAsState().value

    val moodCountMap: MutableMap<String, Int> = remember {
        mutableMapOf(
            "Fun" to 0,
            "Cry" to 0,
            "Sad" to 0,
            "Disgust" to 0,
            "Fear" to 0,
            "Angry" to 0
        )
    }

    LaunchedEffect(diaryList) {
        moodCountMap.entries.forEach { it.setValue(0) }
        for (diary in diaryList) {
            moodCountMap[diary.mood] = moodCountMap[diary.mood]?.plus(1) ?: 0
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Report",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
            )
        }
    ) { paddingValue ->
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValue)
        ) {
            DiaryCountWrapper(
                modifier = Modifier.padding(horizontal = 16.dp),
                countDiary = diaryViewModel.diaryList.collectAsState().value.size,
                countStreak = 1
            )
            Spacer(modifier = Modifier.height(16.dp))
            BieuDoTamTrang(
                modifier = Modifier.padding(horizontal = 16.dp),
                moodCountMap = moodCountMap
            )
            Spacer(modifier = Modifier.height(16.dp))
            ThongKeTamTrang(
                modifier = Modifier.padding(horizontal = 16.dp),
                moodCountMap = moodCountMap
            )
        }   
    }
}
@Composable
fun DiaryCountWrapper(
    modifier: Modifier = Modifier,
    countDiary: Int,
    countStreak: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = countDiary.toString(),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = stringResource(com.uit.melodydiary.R.string.title_nhat_ky),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = countStreak.toString(),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = stringResource(com.uit.melodydiary.R.string.title_chuoi_ghi_lien_tuc),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BieuDoTamTrang(
    modifier: Modifier = Modifier,
    moodCountMap: Map<String, Int>
) {
    val sumCount = moodCountMap.values.sum()
    Column(
        modifier = modifier.fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .padding(vertical = 24.dp),
    ) {
        Text(
            text = stringResource(com.uit.melodydiary.R.string.title_bieu_do_tam_trang),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 24.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PieChart(
                modifier = modifier.size(100.dp),
                data = mapOf(
                    Pair("Fun", moodCountMap["Fun"]!!),
                    Pair("Cry", moodCountMap["Cry"]!!),
                    Pair("Sad", moodCountMap["Sad"]!!),
                    Pair("Disgust", moodCountMap["Disgust"]!!),
                    Pair("Fear", moodCountMap["Fear"]!!),
                    Pair("Angry", moodCountMap["Angry"]!!),
                )
            )
            Column {
                TamTrangItem(
                    color = Color.Blue,
                    tamTrang = "Fun",
                    phanTram = (moodCountMap["Fun"]!!.toFloat() / sumCount * 100).toInt()
                )
                TamTrangItem(
                    color = Color.Magenta,
                    tamTrang = "Cry",
                    phanTram = (moodCountMap["Cry"]!!.toFloat() / sumCount * 100).toInt()
                )
                TamTrangItem(
                    color = Color.Green,
                    tamTrang = "Sad",
                    phanTram = (moodCountMap["Sad"]!!.toFloat() / sumCount * 100).toInt()
                )
                TamTrangItem(
                    color = Color.DarkGray,
                    tamTrang = "Disgust",
                    phanTram = (moodCountMap["Disgust"]!!.toFloat() / sumCount * 100).toInt()
                )
                TamTrangItem(
                    color = Color.Yellow,
                    tamTrang = "Fear",
                    phanTram = (moodCountMap["Fear"]!!.toFloat() / sumCount * 100).toInt()
                )
                TamTrangItem(
                    color = Color.Red,
                    tamTrang = "Angry",
                    phanTram = (moodCountMap["Angry"]!!.toFloat() / sumCount * 100).toInt()
                )
            }
        }
    }
}

@Composable
fun TamTrangItem(
    modifier: Modifier = Modifier,
    color: Color,
    tamTrang: String,
    phanTram: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = modifier.background(
                color, shape = RoundedCornerShape(100.dp)
            )
                .size(12.dp),
        )
        Text(
            text = tamTrang,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = "$phanTram%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun ThongKeTamTrang(
    modifier: Modifier = Modifier,
    moodCountMap: Map<String, Int>
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .padding(vertical = 24.dp),
    ) {
        Text(
            text = stringResource(com.uit.melodydiary.R.string.title_thong_ke_tam_trang),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 24.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        MyBarChartParent(moodCountMap)
    }
}
@Composable
fun MyBarChartParent(
    moodCountMap: Map<String, Int>
) {
    BarChart(
        barChartData = BarChartData(bars = listOf(
            BarChartData.Bar(
                label = "Fun",
                value = moodCountMap["Fun"]!!.toFloat(),
                color = Blue
            ),
            BarChartData.Bar(
                label = "Cry",
                value = moodCountMap["Cry"]!!.toFloat(),
                color = Color.Magenta
            ),
            BarChartData.Bar(
                label = "Sad",
                value = moodCountMap["Sad"]!!.toFloat(),
                color = Color.Green
            ),
            BarChartData.Bar(
                label = "Disgust",
                value = moodCountMap["Disgust"]!!.toFloat(),
                color = Color.DarkGray
            ),
            BarChartData.Bar(
                label = "Fear",
                value = moodCountMap["Fear"]!!.toFloat(),
                color =Color.Yellow
            ),
            BarChartData.Bar(
                label = "Angry",
                value = moodCountMap["Angry"]!!.toFloat(),
                color = Red
            )
        ),
            startAtZero = true,
            padBy = 10f
        ),
        modifier = Modifier.padding(horizontal = 20.dp),
        labelDrawer = SimpleValueDrawer(
            labelTextSize = 8.sp,
            labelTextColor = Color.Black,
            drawLocation = SimpleValueDrawer.DrawLocation.Outside
        )

    )
}
@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    data: Map<String, Int>,
    radiusOuter: Dp = 140.dp,
    chartBarWidth: Dp = 15.dp,
    animDuration: Int = 1000,
) {

    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    val colors = listOf(
        Blue,
        Color.Magenta,
        Color.Green,
        Color.DarkGray,
        Color.Yellow,
        Red
    )

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .offset { IntOffset.Zero }
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }


    }

}
@Preview(showBackground = true)
@Composable
fun PreviewReportScreen() {
    MelodyDiaryTheme {
        val diaryViewModel: DiaryViewModel = viewModel(factory = DiaryViewModel.Factory)
        ReportScreen(diaryViewModel = diaryViewModel, navController = rememberNavController())
    }
}