package com.example.projecttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.model.TourAttractionSearchInfo
import com.example.projecttravel.model.CityInfo
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.model.InterestInfo
import com.example.projecttravel.model.TourAttractionInfo
import com.example.projecttravel.model.TourAttractionAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class SelectPageViewModel : ViewModel() {
    /** all selection */
    private val _uiState = MutableStateFlow(SelectUiState())
    val selectUiState: StateFlow<SelectUiState> = _uiState.asStateFlow()

    /** setDateRange Object */
    fun setDateRange(desiredDateRange: ClosedRange<LocalDate>?) {
        _uiState.update { currentState ->
            currentState.copy(selectDateRange = desiredDateRange)
        }
    }

    /** setCountry Object */
    fun setCountry(desiredCountry: CountryInfo?) {
        _uiState.update { currentState ->
            currentState.copy(selectCountry = desiredCountry)
        }
    }

    /** setCity Object */
    fun setCity(desiredCity: CityInfo?) {
        _uiState.update { currentState ->
            currentState.copy(selectCity = desiredCity)
        }
    }

    /** setInterest Object */
    fun setInterest(desiredInterest: InterestInfo?) {
        _uiState.update { currentState ->
            currentState.copy(selectInterest = desiredInterest)
        }
    }

    /** setSearch Object */
    fun setSearch(desiredSearch: TourAttractionSearchInfo?) {
        _uiState.update { currentState ->
            currentState.copy(selectSearch = desiredSearch)
        }
    }

    /** SETTING List<TourAttractionAll> */
    /** add TourAttraction Object */
    fun addTourAttraction(desiredTourAttraction: TourAttractionInfo) {
        _uiState.update { currentState ->
            val updatedList = currentState.selectTourAttractions.toMutableList()
            if (!updatedList.contains(desiredTourAttraction)) {
                updatedList.add(desiredTourAttraction)
                currentState.copy(selectTourAttractions = updatedList)
            } else {
                currentState // 이미 추가된 객체이므로 상태를 변경하지 않음
            }
        }
    }

    /** remove TourAttraction Object */
    fun cancelTourAttraction(desiredTourAttraction: TourAttractionAll) {
        _uiState.update { currentState ->
            val updatedList = currentState.selectTourAttractions.toMutableList()
            if (updatedList.contains(desiredTourAttraction)) {
                updatedList.remove(desiredTourAttraction) // 선택된 관광지를 리스트에서 제거
                currentState.copy(selectTourAttractions = updatedList)
            } else {
                currentState // 이미 추가된 객체이므로 상태를 변경하지 않음
            }
        }
    }

    /** add TourAttrSearch Object */
    fun addTourAttrSearch(desiredTourAttraction: TourAttractionSearchInfo) {
        _uiState.update { currentState ->
            val updatedList = currentState.selectTourAttractions.toMutableList()
            if (!updatedList.contains(desiredTourAttraction)) {
                updatedList.add(desiredTourAttraction)
                currentState.copy(selectTourAttractions = updatedList)
            } else {
                currentState // 이미 추가된 객체이므로 상태를 변경하지 않음
            }
        }
    }

//    /** remove TourAttrSearch Object */
//    fun cancelTourAttrSearch(desiredTourAttraction: TourAttractionAll) {
//        _uiState.update { currentState ->
//            val updatedList = currentState.selectTourAttractions.toMutableList()
//            if (updatedList.contains(desiredTourAttraction)) {
//                updatedList.remove(desiredTourAttraction) // 선택된 관광지를 리스트에서 제거
//                currentState.copy(selectTourAttractions = updatedList)
//            } else {
//                currentState // 이미 추가된 객체이므로 상태를 변경하지 않음
//            }
//        }
//    }
//
//    /** reset all Objects Except DateRange  */
//    fun resetExceptDateRangeSelectUiState() {
//        _uiState.value = SelectUiState() // DailyPlanUiState를 기본 값으로 재설정
//    }

    /** reset all Objects */
    fun resetAllSelectUiState() {
        _uiState.value = SelectUiState() // DailyPlanUiState를 기본 값으로 재설정
    }
}
