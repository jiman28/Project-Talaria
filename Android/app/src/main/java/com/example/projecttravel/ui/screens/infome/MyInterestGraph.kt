package com.example.projecttravel.ui.screens.infome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.R
import com.example.projecttravel.model.UserInterest
import com.example.projecttravel.model.UserRadarChartInfo
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInterestGraph(
    filteredInfoGraph: UserInterest,
    onDismiss: () -> Unit,
) {

    val userRadarData = UserRadarChartInfo(
        sights = "0.${filteredInfoGraph.sights}".toFloat(),
        nature = "0.${filteredInfoGraph.nature}".toFloat(),
        culture = "0.${filteredInfoGraph.culture}".toFloat(),
        history = "0.${filteredInfoGraph.history}".toFloat(),
        food = "0.${filteredInfoGraph.food}".toFloat(),
        religion = "0.${filteredInfoGraph.religion}".toFloat(),
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        content = {
            Card(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Column (
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    modifier = Modifier
                        .width(370.dp)  // 해당 composable 의 가로 크기 고정
                        .padding(10.dp)
                ) {
                    Text(
                        text = "나의 성향 확인",
                        fontSize = 30.sp,   // font 의 크기
                        fontWeight = FontWeight.Light,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.titleLarge,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        modifier = Modifier
                            .padding(10.dp) // 원하는 여백을 추가(start = 15.dp, end = 15.dp, ...)
                            .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                        ,
                    )

                    Spacer(modifier = Modifier.padding(5.dp))
                    RadarChart(
                        dataPoints = listOf(
                            userRadarData.sights,
                            userRadarData.nature,
                            userRadarData.culture,
                            userRadarData.history,
                            userRadarData.food,
                            userRadarData.religion,
                        ),
                        labels = listOf("Sights", "Nature", "Culture", "History", "Food", "Religion"),
                    )
                    Text(
                        text = "  명소 = ${filteredInfoGraph.sights} point",
                        fontSize = 20.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = "  자연 = ${filteredInfoGraph.nature} point",
                        fontSize = 20.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = "  문화 = ${filteredInfoGraph.culture} point",
                        fontSize = 20.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = "  역사 = ${filteredInfoGraph.history} point",
                        fontSize = 20.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = "  음식 = ${filteredInfoGraph.food} point",
                        fontSize = 20.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = "  종교 = ${filteredInfoGraph.religion} point",
                        fontSize = 20.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    )
}
