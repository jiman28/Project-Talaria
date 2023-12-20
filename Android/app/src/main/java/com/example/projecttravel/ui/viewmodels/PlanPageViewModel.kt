package com.example.projecttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.model.CheckSingleDayTrip
import com.example.projecttravel.model.TourAttractionAll
import com.example.projecttravel.model.SpotDto
import com.example.projecttravel.model.SpotDtoResponse
import com.example.projecttravel.model.WeatherResponseGet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class PlanPageViewModel : ViewModel() {
    /** all selection */
    private val _uiState = MutableStateFlow(PlanUiState())
    val planUiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

    /** setWeatherSwitch Object */
    fun setWeatherSwitch(onOff: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(weatherSwitch = onOff)
        }
    }

    /** setPlanDateRange Object */
    fun setPlanDateRange(desiredDateRange: ClosedRange<LocalDate>?) {
        _uiState.update { currentState ->
            currentState.copy(planDateRange = desiredDateRange)
        }
    }

    /** setName Object */
    fun setCurrentPlanDate (desiredDate: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(currentPlanDate = desiredDate)
        }
    }

    /** setPlanTourAttr Object */
    fun setPlanTourAttr(desiredTourAttraction: List<TourAttractionAll>) {
        _uiState.update { currentState ->
            currentState.copy(planTourAttractionAll = desiredTourAttraction)
        }
    }

    /** setDateToWeather Object */
    fun setDateToWeather(desiredTourAttraction: List<WeatherResponseGet>) {
        _uiState.update { currentState ->
            currentState.copy(dateToWeather = desiredTourAttraction)
        }
    }

    /** setDateToAttrByWeather Object */
    fun setDateToAttrByWeather(desiredTourAttraction: List<SpotDtoResponse>) {
        _uiState.update { currentState ->
            currentState.copy(dateToAttrByWeather = desiredTourAttraction)
        }
    }

    /** setDateToAttrByRandom Object */
    fun setDateToAttrByCity(desiredTourAttraction: List<SpotDtoResponse>) {
        _uiState.update { currentState ->
            currentState.copy(dateToAttrByCity = desiredTourAttraction)
        }
    }

    /** setPlanTourAttrMap Object */
    fun setPlanTourAttrMap(desiredTourAttraction: Map<LocalDate, List<TourAttractionAll>>) {
        _uiState.update { currentState ->
            currentState.copy(dateToSelectedTourAttrMap = desiredTourAttraction)
        }
    }

//    /** moveAttrByWeather Object */
//    fun moveAttrByWeather(destinationDate: LocalDate, spotDtoToMove: SpotDto){
//        _uiState.update { currentState ->
//            // 복사본 생성
//            val updatedAttrByWeather = currentState.dateToAttrByWeather.toMutableList()
//
//            // destinationDate에 해당하는 SpotDtoResponse 찾기
//            val destinationSpotDtoResponseWeather = updatedAttrByWeather.find { it.date == destinationDate.toString() }
//
//            if (destinationSpotDtoResponseWeather != null) {
//                destinationSpotDtoResponseWeather.list = destinationSpotDtoResponseWeather.list.toMutableList()
//                if ((destinationSpotDtoResponseWeather.list as MutableList<SpotDto>).contains(spotDtoToMove)) {
//                    Log.d("xxxxxxxxxxxxxxxxxxxx", "이미 있는 거임")
//                } else {
//                    (destinationSpotDtoResponseWeather.list as MutableList<SpotDto>).add(spotDtoToMove)
//                }
//            }
//
//            // 업데이트된 목록을 새로운 PlanUiState로 복사하여 반환
//            currentState.copy(dateToAttrByWeather = updatedAttrByWeather)
//        }
//    }

    /** removeAttrByWeather Object */
    fun removeAttrByWeather(sourceDate: LocalDate, spotDtoToMove: SpotDto) {
        _uiState.update { currentState ->
            // 복사본 생성
            val updatedAttrByRandom = currentState.dateToAttrByWeather.toMutableList()
            // sourceDate와 destinationDate에 해당하는 SpotDtoResponse 찾기
            val sourceSpotDtoResponse = updatedAttrByRandom.find { it.date == sourceDate.toString() }
            (sourceSpotDtoResponse?.list as MutableList<SpotDto>).remove(spotDtoToMove)
            // destinationSpotDtoResponse에 spotDtoToMove 추가
            currentState.copy(dateToAttrByWeather = updatedAttrByRandom)
        }
    }

    /** addAttrByWeather Object */
    fun addAttrByWeather(destinationDate: LocalDate, spotDtoToMove: SpotDto) {
        _uiState.update { currentState ->
            // 복사본 생성
            val updatedAttrByRandom = currentState.dateToAttrByWeather.toMutableList()
            // sourceDate와 destinationDate에 해당하는 SpotDtoResponse 찾기
            val destinationSpotDtoResponse = updatedAttrByRandom.find { it.date == destinationDate.toString() }
            (destinationSpotDtoResponse?.list as MutableList<SpotDto>).add(spotDtoToMove)
            currentState.copy(dateToAttrByWeather = updatedAttrByRandom)
        }
    }

    /** removeAttrByRandom Object */
    fun removeAttrByRandom(sourceDate: LocalDate, spotDtoToMove: SpotDto) {
        _uiState.update { currentState ->
            // 복사본 생성
            val updatedAttrByRandom = currentState.dateToAttrByCity.toMutableList()
            // sourceDate와 destinationDate에 해당하는 SpotDtoResponse 찾기
            val sourceSpotDtoResponse = updatedAttrByRandom.find { it.date == sourceDate.toString() }
            (sourceSpotDtoResponse?.list as MutableList<SpotDto>).remove(spotDtoToMove)
            // destinationSpotDtoResponse에 spotDtoToMove 추가
            currentState.copy(dateToAttrByCity = updatedAttrByRandom)
        }
    }

    /** addAttrByRandom Object */
    fun addAttrByRandom(destinationDate: LocalDate, spotDtoToMove: SpotDto) {
        _uiState.update { currentState ->
            // 복사본 생성
            val updatedAttrByRandom = currentState.dateToAttrByCity.toMutableList()
            // sourceDate와 destinationDate에 해당하는 SpotDtoResponse 찾기
            val destinationSpotDtoResponse = updatedAttrByRandom.find { it.date == destinationDate.toString() }
            (destinationSpotDtoResponse?.list as MutableList<SpotDto>).add(spotDtoToMove)
            currentState.copy(dateToAttrByCity = updatedAttrByRandom)
        }
    }

    /** setGpsPage Object */
    fun setGpsPage(singleDay: CheckSingleDayTrip?) {
        _uiState.update { currentState ->
            currentState.copy(checkSingleDayGps = singleDay)
        }
    }

    /** reset all Objects */
    fun resetAllPlanUiState() {
        _uiState.value = PlanUiState() // DailyPlanUiState를 기본 값으로 재설정
    }
}