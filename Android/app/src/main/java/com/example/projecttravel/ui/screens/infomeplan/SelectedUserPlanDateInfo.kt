package com.example.projecttravel.ui.screens.infomeplan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.SpotDtoResponseRead
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@Composable
fun SelectedUserPlanDateInfo(
    allUserAttrList: List<SpotDtoResponseRead>,
    userPageUiState: UserPageUiState,
    planPageViewModel: PlanPageViewModel,
    onRouteClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
        horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
    ) {
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) {
            Text(
                text = userPageUiState.currentMyPlanDate.toString(),
                fontSize = 25.sp,   // font 의 크기
                fontWeight = FontWeight.Bold,  // font 의 굵기
                fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                style = MaterialTheme.typography.titleLarge,  //font 의 스타일
                textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
            )
        }
        Column (
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            OutlinedButton(
                modifier = Modifier
                    .padding(1.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    planPageViewModel.setGpsPage(allUserAttrList.find { it.date == userPageUiState.currentMyPlanDate.toString() })
                    onRouteClicked()
                }
            ) {
                Icon(imageVector = Icons.Filled.Map, contentDescription = "DoneAll")
                Text(
                    text = " 지도 확인",
                    fontWeight = FontWeight.Thin,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    style = MaterialTheme.typography.titleSmall,  //font 의 스타일
                    textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                )
            }
        }
    }

}
