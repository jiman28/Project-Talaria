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
import com.example.projecttravel.data.repositories.TourAttractionListRepository
import com.example.projecttravel.model.TourAttractionInfo
import kotlinx.coroutines.launch

sealed interface TourAttractionUiState {

    data class TourAttractionSuccess(val tourAttractionList: List<TourAttractionInfo>) :
        TourAttractionUiState
}

class ListTourAttrRepoViewModel(private val tourAttractionListRepository: TourAttractionListRepository) : ViewModel() {

    var tourAttractionUiState: TourAttractionUiState by mutableStateOf(
        TourAttractionUiState.TourAttractionSuccess(
            emptyList()
        )
    )
        private set

    init {
        getTourAttraction()
    }

    fun getTourAttraction() {
        viewModelScope.launch {
            try {
                val tourAttractionList = tourAttractionListRepository.getTourAttractionList()
                tourAttractionUiState =
                    TourAttractionUiState.TourAttractionSuccess(tourAttractionList)
            } catch (e: Exception) {
                // Handle the error case if necessary
            }
        }
    }

    companion object {
        val TourAttractionFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as TravelApplication)
                val tourAttractionListRepository = application.container.tourAttractionListRepository
                ListTourAttrRepoViewModel(tourAttractionListRepository = tourAttractionListRepository)
            }
        }
    }
}