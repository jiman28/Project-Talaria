package com.example.projecttravel.ui.screens.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.ui.viewmodels.CountryUiState
import com.example.projecttravel.ui.viewmodels.ListCountryRepoViewModel
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel
import com.example.projecttravel.model.CityInfo
import com.example.projecttravel.model.InterestInfo
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.viewmodels.CityUiState
import com.example.projecttravel.ui.viewmodels.ListCityRepoViewModel
import com.example.projecttravel.ui.viewmodels.InterestUiState
import com.example.projecttravel.ui.viewmodels.ListInterestRepoViewModel
import com.example.projecttravel.ui.viewmodels.TourAttractionUiState
import com.example.projecttravel.ui.viewmodels.ListTourAttrRepoViewModel

@Composable
fun SelectPage(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planPageViewModel: PlanPageViewModel,
    selectUiState: SelectUiState,
    selectPageViewModel: SelectPageViewModel,
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    onGpsClicked: () -> Unit = {},
) {
    val listCountryRepoViewModel: ListCountryRepoViewModel = viewModel(factory = ListCountryRepoViewModel.CountryFactory)
    val listCityRepoViewModel: ListCityRepoViewModel = viewModel(factory = ListCityRepoViewModel.CityFactory)
    val listInterestRepoViewModel: ListInterestRepoViewModel = viewModel(factory = ListInterestRepoViewModel.InterestFactory)
    val listTourAttrRepoViewModel: ListTourAttrRepoViewModel = viewModel(factory = ListTourAttrRepoViewModel.TourAttractionFactory)

    val selectedCountry by remember { mutableStateOf<CountryInfo?>(null) }
    val selectedCity by remember { mutableStateOf<CityInfo?>(null) }
    val selectedInterest by remember { mutableStateOf<InterestInfo?>(null) }

    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        modifier = Modifier
            .padding(10.dp) // 원하는 여백을 추가(start = 15.dp, end = 15.dp, ...)
            .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
    ) {
        /** Buttons ====================*/
        Column {
            SelectPageButtons(
                userPageUiState = userPageUiState,
                userPageViewModel = userPageViewModel,
                planPageViewModel = planPageViewModel,
                selectUiState = selectUiState,
                selectPageViewModel = selectPageViewModel,
                onCancelButtonClicked = onCancelButtonClicked,
                onNextButtonClicked = onNextButtonClicked,
            )
        }
        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

        /** Selections ====================*/
        Column {
            Column {
                SelectDateRange(
                    selectUiState = selectUiState,
                    selectPageViewModel = selectPageViewModel,
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                /** ================================================== */
                /** Country Selection */
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(1.dp),
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(1.dp),
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
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
                /** Interest Selection */
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(1.dp),
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                ) {
                    val interestUiState =
                        (listInterestRepoViewModel.interestUiState as? InterestUiState.InterestSuccess)
                    if (interestUiState != null) {
                        SelectInterest(
                            selectUiState = selectUiState,
                            interestUiState = interestUiState,
                            selectPageViewModel = selectPageViewModel,
                            selectedCountry = selectUiState.selectCountry,
                            selectedCity = selectUiState.selectCity,
                            selectedInterest = selectedInterest,
                        )
                    }
                }
                /** ================================================== */
            }
        }
        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

        /** ================================================== */
        /** Show your Selection ====================*/
        Column(
        ) {
            SelectedTourAttractions(
                selectUiState = selectUiState,
                selectPageViewModel = selectPageViewModel,
                contentPadding = PaddingValues(5.dp),
            )
        }
        Divider(thickness = 4.dp)
        Spacer(modifier = Modifier.height(2.dp))

        /** ================================================== */
        /** Tour Attraction Selection ====================*/
        Column(
        ) {
            val tourAttractionUiState =
                (listTourAttrRepoViewModel.tourAttractionUiState as? TourAttractionUiState.TourAttractionSuccess)
            if (tourAttractionUiState != null) {
                ShowTourAttractionInfos(
                    onGpsClicked = onGpsClicked,
                    selectUiState = selectUiState,
                    selectPageViewModel = selectPageViewModel,
                    tourAttractionUiState = tourAttractionUiState,
                    selectedCountry = selectUiState.selectCountry,
                    selectedCity = selectUiState.selectCity,
                    selectedInterest = selectUiState.selectInterest,
                )
            }
        }
    }
}
