package com.example.projecttravel.ui.screens.searchplace

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.SearchUiState
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.model.CityInfo
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.ui.screens.searchplace.searchapi.findSearchListByName
import com.example.projecttravel.ui.screens.select.SelectCity
import com.example.projecttravel.ui.screens.select.SelectCountry
import com.example.projecttravel.ui.viewmodels.SearchViewModel
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel
import com.example.projecttravel.ui.viewmodels.CityUiState
import com.example.projecttravel.ui.viewmodels.ListCityRepoViewModel
import com.example.projecttravel.ui.viewmodels.CountryUiState
import com.example.projecttravel.ui.viewmodels.ListCountryRepoViewModel
import com.example.projecttravel.ui.viewmodels.TourAttrSearchUiState
import com.example.projecttravel.ui.viewmodels.ListTourSearchRepoViewModel

@Composable
fun SearchGpsPage(
    selectUiState: SelectUiState,
    searchUiState: SearchUiState,
    searchViewModel: SearchViewModel,
    selectPageViewModel: SelectPageViewModel,
    onBackButtonClicked: () -> Unit = {},
    updateUiPageClicked: () -> Unit = {},
) {
    var searchedPlaceId: String by remember { mutableStateOf("") }

    val listCountryRepoViewModel: ListCountryRepoViewModel = viewModel(factory = ListCountryRepoViewModel.CountryFactory)
    val listCityRepoViewModel: ListCityRepoViewModel = viewModel(factory = ListCityRepoViewModel.CityFactory)

    val selectedCountry by remember { mutableStateOf<CountryInfo?>(null) }
    val selectedCity by remember { mutableStateOf<CityInfo?>(null) }

    val listTourSearchRepoViewModel: ListTourSearchRepoViewModel = viewModel(factory = ListTourSearchRepoViewModel.TourAttrSearchFactory)
    val tourAttrSearchUiState = (listTourSearchRepoViewModel.tourAttrSearchUiState as? TourAttrSearchUiState.TourAttrSearchSuccess)
    if (tourAttrSearchUiState != null) {
        findSearchListByName(
            searchUiState.searchedPlace?.name,
            tourAttrSearchUiState.tourAttrSearchList
        )?.let { selectPageViewModel.setSearch(it) }

        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
        ) {
            /** Buttons */
            Column {
                SearchGpsPageButton(
                    selectUiState = selectUiState,
                    searchViewModel = searchViewModel,
                    selectPageViewModel = selectPageViewModel,
                    onBackButtonClicked = onBackButtonClicked,
                    updateUiPageClicked = updateUiPageClicked,
                )
            }

            /** Selections */
            Divider(thickness = dimensionResource(R.dimen.thickness_divider3))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                /** ================================================== */
                /** Country Selection */
                Column(
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(3.dp),
                ) {
                    val countryUiState =
                        (listCountryRepoViewModel.countryUiState as? CountryUiState.CountrySuccess)
                    if (countryUiState != null) {
                        SelectCountry(
                            selectUiState = selectUiState,
                            countryUiState = countryUiState,
                            selectPageViewModel = selectPageViewModel,
                            selectedCountry = selectedCountry,
                        )
                    }
                }
                /** ================================================== */
                /** City Selection */
                Column(
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(3.dp),
                ) {
                    val cityUiState =
                        (listCityRepoViewModel.cityUiState as? CityUiState.CitySuccess)
                    if (cityUiState != null) {
                        SelectCity(
                            selectUiState = selectUiState,
                            cityUiState = cityUiState,
                            selectPageViewModel = selectPageViewModel,
                            selectedCountry = selectUiState.selectCountry,
                            selectedCity = selectedCity,
                        )
                    }
                }
                /** ================================================== */
                /** InOut Selection */
                Column(
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(3.dp),
                ) {
                    InOutSelect(
                        searchUiState = searchUiState,
                        searchViewModel = searchViewModel,
                    )
                }
            }

            /** all result comes out here ================================================== */
            Column {
                GoogleMapSheet(
                    searchedPlaceId = searchedPlaceId,
                    selectUiState = selectUiState,
                    searchUiState = searchUiState,
                    searchViewModel = searchViewModel,
                    updateUiPageClicked = updateUiPageClicked,
                )  { newPlaceName -> searchedPlaceId = newPlaceName }
            }
        }
    }
    /** SearchGpsDialog Screen closed when click phone's backButton */
    /** Must be placed inside 'if (isDatePickerVisible)' to apply this BackHandler logic to this Composable */
    BackHandler(
        onBack = onBackButtonClicked
    )
}
