package com.example.projecttravel.ui.screens.select.selectdialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.model.TourAttractionAll
import com.example.projecttravel.model.TourAttractionInfo
import com.example.projecttravel.model.TourAttractionSearchInfo
import com.example.projecttravel.model.SpotDto
import com.example.projecttravel.model.SpotDtoResponse
import com.example.projecttravel.ui.screens.select.selectapi.getDateToAttrByCity
import com.example.projecttravel.ui.screens.select.selectapi.getDateToAttrByWeather
import com.example.projecttravel.ui.screens.select.selectapi.getDateToWeather
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

/** ===================================================================== */
/** PlanConfirmDialog to ask whether to go planPage or not ====================*/
@Composable
fun PlanConfirmDialog(
    planPageViewModel: PlanPageViewModel,
    selectUiState: SelectUiState,
    onNextButtonClicked: () -> Unit = {},
    onDismiss: () -> Unit,
    onLoadingStarted: () -> Unit,
    onErrorOccurred: () -> Unit
) {
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "선택사항들로 계획표를 만듭니다",
                fontSize = 20.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                modifier = Modifier
                    .padding(10.dp) // 원하는 여백을 추가).
                    .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
            )
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            onLoadingStarted()
                            planPageViewModel.setPlanTourAttr(selectUiState.selectTourAttractions)
                            planPageViewModel.setPlanTourAttrMap(createDefaultDateToSelectedTourAttractions(selectUiState))
                            // 비동기 작업을 시작하고 결과(return)를 받아오기 위한 Deferred 객체를 생성합니다.
                            val weatherDeferred = async { getDateToWeather(selectUiState, planPageViewModel) }
                            val attrWeatherDeferred = async { getDateToAttrByWeather(selectUiState, planPageViewModel) }
                            val attrCityDeferred = async { getDateToAttrByCity(selectUiState, planPageViewModel) }

                            // Deferred 객체의 await() 함수를 사용하여 작업 완료를 대기하고 결과를 받아옵니다.
                            val isWeatherComplete = weatherDeferred.await()
                            val isAttrWeatherComplete = attrWeatherDeferred.await()
                            val isAttrCityComplete = attrCityDeferred.await()

                            // 모든 작업이 완료되었을 때만 실행합니다.
                            if (isWeatherComplete && isAttrWeatherComplete && isAttrCityComplete) {
                                planPageViewModel.setPlanDateRange(selectUiState.selectDateRange)
                                selectUiState.selectDateRange?.let {
                                    planPageViewModel.setCurrentPlanDate(
                                        it.start)
                                }
                                onDismiss()
                                onNextButtonClicked()
                            } else {
                                onDismiss()
                                onErrorOccurred()
                            }
                        }
                    }
                ) {
                    Text(text = "확인", fontSize = 20.sp)
                }
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "취소", fontSize = 20.sp)
                }
            }
        },
    )
}

// Map 형태로 변환
fun createDefaultDateToSelectedTourAttractions(
    selectUiState: SelectUiState,
): Map<LocalDate, List<TourAttractionAll>> {
    val result = mutableMapOf<LocalDate, List<TourAttractionAll>>()
    if (selectUiState.selectDateRange != null && selectUiState.selectTourAttractions.isNotEmpty()) {
        val dateRange = selectUiState.selectDateRange
        val mutableAttractionList = selectUiState.selectTourAttractions.toMutableList()
        val totalAttractionCount = mutableAttractionList.size
        val daysCount = dateRange.endInclusive.toEpochDay() - dateRange.start.toEpochDay() + 1
        val attractionsPerDay = totalAttractionCount / daysCount
        var remainingAttractions = totalAttractionCount % daysCount

        if (mutableAttractionList.isNotEmpty()) {
            var currentDate = dateRange.start
            while (!currentDate.isAfter(dateRange.endInclusive)) {
                val attractionsForDate = if (remainingAttractions > 0) {
                    mutableAttractionList.take((attractionsPerDay + 1).toInt())
                        .also { remainingAttractions-- }
                } else {
                    mutableAttractionList.take(attractionsPerDay.toInt())
                }
                result[currentDate] = attractionsForDate
                mutableAttractionList.removeAll(attractionsForDate)
                currentDate = currentDate.plusDays(1)
            }
        }
    }
    return result
}

// Map 형태로 변환 후 다시 ArrayList 형태로 변환
fun convertToSpotDtoResponses(
    selectUiState: SelectUiState,
): List<SpotDtoResponse> {
    val result = mutableMapOf<LocalDate, List<TourAttractionAll>>()
    if (selectUiState.selectDateRange != null && selectUiState.selectTourAttractions.isNotEmpty()) {
        val dateRange = selectUiState.selectDateRange
        val mutableAttractionList = selectUiState.selectTourAttractions.toMutableList()
        val totalAttractionCount = mutableAttractionList.size
        val daysCount = dateRange.endInclusive.toEpochDay() - dateRange.start.toEpochDay() + 1
        val attractionsPerDay = totalAttractionCount / daysCount
        var remainingAttractions = totalAttractionCount % daysCount

        if (mutableAttractionList.isNotEmpty()) {
            var currentDate = dateRange.start
            while (!currentDate.isAfter(dateRange.endInclusive)) {
                val attractionsForDate = if (remainingAttractions > 0) {
                    mutableAttractionList.take((attractionsPerDay + 1).toInt())
                        .also { remainingAttractions-- }
                } else {
                    mutableAttractionList.take(attractionsPerDay.toInt())
                }
                result[currentDate] = attractionsForDate
                mutableAttractionList.removeAll(attractionsForDate)
                currentDate = currentDate.plusDays(1)
            }
        }
    }
    val realResult = result.map { (date, attractionList) ->
        val spotList = attractionList.map { attraction ->
            when (attraction) {
                is TourAttractionInfo ->
                    SpotDto(
                        pk = attraction.placeId,
                        name = attraction.placeName,
                        img = attraction.imageP,
                        lan = attraction.lan,
                        lat = attraction.lat,
                        inOut = attraction.inOut,
                        cityId = attraction.cityId
                    )
                is TourAttractionSearchInfo ->
                    SpotDto(
                        pk = attraction.name,
                        name = attraction.name,
                        img = attraction.img,
                        lan = attraction.lng,
                        lat = attraction.lat,
                        inOut = attraction.inOut,
                        cityId = attraction.cityId
                    )
                else -> null // 처리되지 않는 다른 형식의 TourAttractionAll에 대한 처리
            }
        }.filterNotNull() // null이 아닌 경우만 남김
        SpotDtoResponse(date.toString(), spotList)
    }
    return realResult
}
