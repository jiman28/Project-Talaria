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
import com.example.projecttravel.data.repositories.CityListRepository
import com.example.projecttravel.model.CityInfo
import kotlinx.coroutines.launch

sealed interface CityUiState {

    data class CitySuccess(val cityList: List<CityInfo>) : CityUiState
}

class ListCityRepoViewModel(private val cityListRepository: CityListRepository) : ViewModel() {

    var cityUiState: CityUiState by mutableStateOf(CityUiState.CitySuccess(emptyList()))
        private set

    init {
        getCity()
    }

    fun getCity() {
        viewModelScope.launch {
            try {
                val cityList = cityListRepository.getCityList()
                cityUiState = CityUiState.CitySuccess(cityList)
            } catch (e: Exception) {
                // Handle the error case if necessary
            }
        }
    }

    companion object {
        val CityFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as TravelApplication)
                val cityRepository = application.container.cityListRepository
                ListCityRepoViewModel(cityListRepository = cityRepository)
            }
        }
    }
}