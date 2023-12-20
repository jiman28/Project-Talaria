package com.example.projecttravel.ui.screens.plantrip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@Composable
fun WeatherSwitchButton(
    planUiState: PlanUiState,
    planPageViewModel: PlanPageViewModel,
) {
    var weatherSwitchChecked by remember { mutableStateOf(planUiState.weatherSwitch) }
    // You can display the selected date if needed
    Row(
        verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
        horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFD4E3FF),
            ),
    ) {
        Column(
            modifier = Modifier
                .weight(3f),
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        ) {
            if (weatherSwitchChecked) {
                Row (
                    verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                    horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                ) {
                    Icon(imageVector = Icons.Filled.WbCloudy, contentDescription = "WbCloudy")
                    Text(
                        text = " 날씨 모드 ON !!!",
                        fontSize = 25.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                    )
                }

            } else {
                Row (
                    verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                    horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                ) {
                    Icon(imageVector = Icons.Filled.WbSunny, contentDescription = "WbSunny")
                    Text(
                        text = " 날씨 모드 OFF  ",
                        fontSize = 25.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.titleLarge,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                    )
                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "ArrowForward")
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        ) {
            Switch(
                checked = weatherSwitchChecked,
                onCheckedChange = { newCheckedState ->
                    weatherSwitchChecked = newCheckedState
                    if (newCheckedState) {
                        // 스위치가 켜졌을 때 실행할 작업
                        planPageViewModel.setWeatherSwitch(true)
                    } else {
                        // 스위치가 꺼졌을 때 실행할 작업
                        planPageViewModel.setWeatherSwitch(false)
                    }
                },
                thumbContent = if (weatherSwitchChecked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Build,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
            )
        }
    }
}
