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
import com.example.projecttravel.data.repositories.InterestListRepository
import com.example.projecttravel.model.InterestInfo
import kotlinx.coroutines.launch

sealed interface InterestUiState {

    data class InterestSuccess(val interestList: List<InterestInfo>) : InterestUiState
}

class ListInterestRepoViewModel(private val interestListRepository: InterestListRepository) : ViewModel() {

    var interestUiState: InterestUiState by mutableStateOf(InterestUiState.InterestSuccess(emptyList()))
        private set

    init {
        getInterest()
    }

    fun getInterest() {
        viewModelScope.launch {
            try {
                val interestList = interestListRepository.getInterestList()
                interestUiState = InterestUiState.InterestSuccess(interestList)
            } catch (e: Exception) {
                // Handle the error case if necessary
            }
        }
    }

    companion object {
        val InterestFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as TravelApplication)
                val interestRepository = application.container.interestListRepository
                ListInterestRepoViewModel(interestListRepository = interestRepository)
            }
        }
    }
}