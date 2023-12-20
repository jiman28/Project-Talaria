package com.example.projecttravel.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projecttravel.TravelApplication
import com.example.projecttravel.data.repositories.TourAttrSearchListRepository
import com.example.projecttravel.model.TourAttractionSearchInfo
import kotlinx.coroutines.launch

sealed interface TourAttrSearchUiState {

    data class TourAttrSearchSuccess(val tourAttrSearchList: List<TourAttractionSearchInfo>) :
        TourAttrSearchUiState
}

class ListTourSearchRepoViewModel(private val tourAttrSearchListRepository: TourAttrSearchListRepository) :
    ViewModel() {

    var tourAttrSearchUiState: TourAttrSearchUiState by mutableStateOf(
        TourAttrSearchUiState.TourAttrSearchSuccess(
            emptyList()
        )
    )
        private set

    init {
        getTourSearch()
    }

    private fun getTourSearch() {
        viewModelScope.launch {
            try {
                val tourAttrSearchList = tourAttrSearchListRepository.getTourAttrSearchList()
                tourAttrSearchUiState =
                    TourAttrSearchUiState.TourAttrSearchSuccess(tourAttrSearchList)
            } catch (e: Exception) {
                // Handle the error case if necessary
            }
        }
    }

    companion object {
        val TourAttrSearchFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as TravelApplication)
                val tourAttrSearchListRepository =
                    application.container.tourAttrSearchListRepository
                ListTourSearchRepoViewModel(tourAttrSearchListRepository = tourAttrSearchListRepository)
            }
        }
    }
}
