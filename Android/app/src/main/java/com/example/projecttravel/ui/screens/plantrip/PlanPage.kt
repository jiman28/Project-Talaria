package com.example.projecttravel.ui.screens.plantrip

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.model.SpotDto
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.SpotDtoResponse
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel

@Composable
fun PlanPage(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planUiState: PlanUiState,
    planPageViewModel: PlanPageViewModel,
    selectPageViewModel: SelectPageViewModel,
    onCancelButtonClicked: () -> Unit,  // 취소버튼 매개변수를 추가
    onPlanCompleteClicked: () -> Unit,
    onRouteClicked: () -> Unit = {},
) {
    var selectedPlanDate by remember { mutableStateOf(planUiState.currentPlanDate) }

    // allList By WeatherSwitch for all list and size
    val allAttrList: List<SpotDtoResponse> = if (planUiState.weatherSwitch) {
        val allAttrList = planUiState.dateToAttrByWeather
        allAttrList
    } else {
        val allAttrList = planUiState.dateToAttrByCity
        allAttrList
    }

    // List By WeatherSwitch for view
    val attrList: List<SpotDto> = allAttrList.find { it.date == selectedPlanDate.toString() }?.list ?: emptyList()

    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        modifier = Modifier.padding(start = 15.dp, end = 15.dp),
    ) {
        /** Buttons ====================*/
        Column {
            PlanPageButtons(
                userPageUiState = userPageUiState,
                userPageViewModel = userPageViewModel,
                planUiState = planUiState,
                planPageViewModel = planPageViewModel,
                selectPageViewModel = selectPageViewModel,
                onCancelButtonClicked = onCancelButtonClicked,
                onPlanCompleteClicked = onPlanCompleteClicked,
            )
        }
        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

        /** ================================================== */
        /** Buttons change between Random & Weather ====================*/
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, // 좌우로 공간을 나눠줌
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherSwitchButton(
                planUiState = planUiState,
                planPageViewModel = planPageViewModel,
            )
        }

        /** ================================================== */
        /** Show your Date List ====================*/
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        ) {
            PlanDateList(
                allAttrList = allAttrList,
                planUiState = planUiState,
                planPageViewModel = planPageViewModel,
                onDateClick = { clickedDate -> selectedPlanDate = clickedDate }
            )
        }
        /** ================================================== */
        /** Show All dateRange ====================*/
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        ) {
            Text(
                text = "${planUiState.planDateRange?.start} ~ ${planUiState.planDateRange?.endInclusive}",
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
            SelectedPlanDateInfo(
                planUiState = planUiState,
                planPageViewModel = planPageViewModel,
                onRouteClicked = onRouteClicked,
            )
            Divider(thickness = dimensionResource(R.dimen.thickness_divider3))
        }

        /** ================================================== */
        /** Show your All Selections ====================*/
        if (planUiState.weatherSwitch) {
            Column {
                if (attrList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier,
                        contentPadding = PaddingValues(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(attrList) { spotDto ->
                            PlanCardTourWeather(
                                planUiState = planUiState,
                                planPageViewModel = planPageViewModel,
                                spotDto = spotDto,
                                onDateClick = { clickedDate ->
                                    selectedPlanDate = clickedDate // 여기서 selectedPlanDate 변경
                                }
                            )
                        }
                    }
                } else {
                    NoPlanListFound()
                }
            }
        } else {
            Column {
                if (attrList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier,
                        contentPadding = PaddingValues(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(attrList) { spotDto ->
                            PlanCardTourAttr(
                                planUiState = planUiState,
                                planPageViewModel = planPageViewModel,
                                spotDto = spotDto,
                                onDateClick = { clickedDate ->
                                    selectedPlanDate = clickedDate // 여기서 selectedPlanDate 변경
                                }
                            )
                        }
                    }
                } else {
                    NoPlanListFound()
                }
            }
        }
    }
}

@Composable
fun NoPlanListFound (
) {
    Spacer(modifier = Modifier.padding(10.dp))
    Text(
        text = "목록이 없습니다.",
        fontSize = 25.sp,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}