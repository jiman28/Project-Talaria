package com.example.projecttravel.ui.screens.infomeplan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.SpotDtoRead
import com.example.projecttravel.model.SpotDtoResponseRead
import com.example.projecttravel.ui.screens.plantrip.NoPlanListFound
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@Composable
fun CheckMyPlanPage(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planUiState: PlanUiState,
    planPageViewModel: PlanPageViewModel,
    onBackButtonClicked: () -> Unit,
    onRouteClicked: () -> Unit,
) {
    var selectedUserPlanDate by remember { mutableStateOf(userPageUiState.currentMyPlanDate) }
    if (userPageUiState.checkMyPageTrip != null) {
        val allUserAttrList: List<SpotDtoResponseRead> = userPageUiState.checkMyPageTrip.plans
        // List By WeatherSwitch for view
        val userAttrList: List<SpotDtoRead> = allUserAttrList.find { it.date == selectedUserPlanDate.toString() }?.list ?: emptyList()

        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
        ) {
            Spacer(Modifier.size(5.dp))

            Column(
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFD4E3FF),
                    ),
            ) {
                Text(
                    text = userPageUiState.checkMyPageTrip.planName,
                    fontSize = 25.sp,   // font 의 크기
                    lineHeight = 25.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
                    fontWeight = FontWeight.Bold,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    style = MaterialTheme.typography.titleLarge,  //font 의 스타일
                    textAlign = TextAlign.Start, // 텍스트 내용을 compose 가운데 정렬
                    modifier = Modifier
                        .padding(10.dp) // 원하는 여백을 추가(start = 15.dp, end = 15.dp, ...)
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                )
            }
            Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

            /** ================================================== */
            /** Show your Date List ====================*/
            Column(
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            ) {
                UserPlanDateList(
                    allUserAttrList = allUserAttrList,
                    userPageViewModel = userPageViewModel,
                    planUiState = planUiState,
                    onDateClick = { clickedDate -> selectedUserPlanDate = clickedDate }
                )
            }
            /** ================================================== */
            /** Show All dateRange ====================*/
            Column(
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            ) {
                Text(
                    text = "${userPageUiState.checkMyPageTrip.startDay} ~ ${userPageUiState.checkMyPageTrip.endDay}",
                    fontSize = 15.sp,   // font 의 크기
                    lineHeight = 15.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
                    fontWeight = FontWeight.Bold,  // font 의 굵기
                    style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                    textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                    modifier = Modifier
                        .padding(5.dp) // 원하는 여백을 추가(start = 15.dp, end = 15.dp, ...)
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                )
            }

            /** ================================================== */
            /** Selected Date Info ====================*/
            Column(
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            ) {
                SelectedUserPlanDateInfo(
                    allUserAttrList = allUserAttrList,
                    userPageUiState = userPageUiState,
                    planPageViewModel = planPageViewModel,
                    onRouteClicked = onRouteClicked,
                )
                Divider(thickness = dimensionResource(R.dimen.thickness_divider3))
            }

            /** ================================================== */
            /** Show your All Selections ====================*/
            if (userAttrList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier,
                    contentPadding = PaddingValues(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(userAttrList) { spotDtoRead ->
                        PlanCardTourUser(
                            spotDtoRead = spotDtoRead,
                        )
                    }
                }
            } else {
                NoPlanListFound()
            }
        }
    }else {
        Text(text = "오류 오류")
    }
}
