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
import com.example.projecttravel.data.repositories.CountryListRepository
import com.example.projecttravel.model.CountryInfo
import kotlinx.coroutines.launch

sealed interface CountryUiState {

    data class CountrySuccess(val countryList: List<CountryInfo>) : CountryUiState
}

class ListCountryRepoViewModel(private val countryListRepository: CountryListRepository) : ViewModel() {

    var countryUiState: CountryUiState by mutableStateOf(CountryUiState.CountrySuccess(emptyList()))
        private set

    init {
        getCountry()
    }

    fun getCountry() {
        viewModelScope.launch {
            try {
                val countryList = countryListRepository.getCountryList()
                countryUiState = CountryUiState.CountrySuccess(countryList)
            } catch (e: Exception) {
                // Handle the error case if necessary
            }
        }
    }

    companion object {
        val CountryFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as TravelApplication)
                val countryRepository = application.container.countryListRepository
                ListCountryRepoViewModel(countryListRepository = countryRepository)
            }
        }
    }
}