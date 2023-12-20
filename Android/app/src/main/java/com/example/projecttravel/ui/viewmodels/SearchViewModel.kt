package com.example.projecttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.projecttravel.data.uistates.SearchUiState
import com.example.projecttravel.model.SearchedPlace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {
    /** all selection */
    private val _uiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    /** setName Object */
    fun setSearched (desiredPlace: SearchedPlace) {
        _uiState.update { currentState ->
            currentState.copy(searchedPlace = desiredPlace)
        }
    }

    /** setInOutChecker */
    fun setInOutChecker (check: String) {
        _uiState.update { currentState ->
            currentState.copy(inOutChecker = check)
        }
    }

    /** setInOutChecker */
    fun setInOutBoolean (check: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(inOutCheckerBoolean = check)
        }
    }

    /** reset all Objects */
    fun resetAllSearchUiState() {
        _uiState.value = SearchUiState() // DailyPlanUiState를 기본 값으로 재설정
    }
}
