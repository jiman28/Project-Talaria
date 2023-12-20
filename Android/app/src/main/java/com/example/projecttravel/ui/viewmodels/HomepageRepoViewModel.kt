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
import retrofit2.HttpException
import java.io.IOException

/** UI state for the Home screen */
sealed interface HomepageUiState {

    data class Success(val countryInfo: List<CountryInfo>) : HomepageUiState
    object Error : HomepageUiState
    object Loading : HomepageUiState
}

/** ViewModel containing the app data and method to retrieve the data */
class HomepageRepoViewModel(private val countryListRepository: CountryListRepository) : ViewModel() {

    var homepageUiState: HomepageUiState by mutableStateOf(HomepageUiState.Loading)
        private set

    init {
        getCountry()
    }

    fun getCountry() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            homepageUiState = try {
                HomepageUiState.Success(countryListRepository.getCountryList())
            } catch (e: IOException) {
                HomepageUiState.Error
            } catch (e: HttpException) {
                HomepageUiState.Error
            }
        }
    }
    /** Factory for TravelViewModel that takes countryInfoRepository as a dependency */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as TravelApplication)
                val travelRepository = application.container.countryListRepository
                HomepageRepoViewModel(countryListRepository = travelRepository)
            }
        }
    }
}
