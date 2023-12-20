package com.example.projecttravel.ui.screens.planroutegps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttravel.R
import com.example.projecttravel.model.SpotDto
import com.example.projecttravel.ui.viewmodels.CityUiState
import com.example.projecttravel.ui.viewmodels.ListCityRepoViewModel
import com.example.projecttravel.ui.viewmodels.CountryUiState
import com.example.projecttravel.ui.viewmodels.ListCountryRepoViewModel

@Composable
fun PlaceViewDialog(
    selectedPlaceMarker: SpotDto,
    onDismiss: () -> Unit,
) {
    val listCityRepoViewModel: ListCityRepoViewModel = viewModel(factory = ListCityRepoViewModel.CityFactory)
    val listCountryRepoViewModel: ListCountryRepoViewModel = viewModel(factory = ListCountryRepoViewModel.CountryFactory)
    val cityUiState = (listCityRepoViewModel.cityUiState as? CityUiState.CitySuccess)
    val countryUiState = (listCountryRepoViewModel.countryUiState as? CountryUiState.CountrySuccess)

    val findCity = cityUiState?.cityList?.find { it.cityId == selectedPlaceMarker.cityId }
    val textCity = findCity?.cityName
    val textCountry = countryUiState?.countryList?.find { it.countryId == findCity?.countryId }?.countryName

    AlertDialog(
    onDismissRequest = onDismiss,
    text = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        ) {
            Column(
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(selectedPlaceMarker.img)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.no_image_country),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                    horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                    modifier = Modifier
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                ) {
                    Text(
                        text = selectedPlaceMarker.name,
                        fontSize = 20.sp,   // font 의 크기
                        lineHeight = 20.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
                        fontWeight = FontWeight.Bold,  // font 의 굵기
                        style = MaterialTheme.typography.titleMedium,  //font 의 스타일
//                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
//                            modifier = Modifier
//                                .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                    )
                }
                Spacer(Modifier.size(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                    horizontalArrangement = Arrangement.End, // 수평 가운데 정렬
                    modifier = Modifier
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                ) {
                    if (textCountry != null) {
                        Text(
                            text = textCountry,
                            fontSize = 10.sp,   // font 의 크기
                            lineHeight = 10.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
                            fontWeight = FontWeight.Normal,  // font 의 굵기
                            style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
//                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
//                            modifier = Modifier
//                                .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                        )
                    }
                    Spacer(Modifier.size(10.dp))
                    if (textCity != null) {
                        Text(
                            text = textCity,
                            fontSize = 10.sp,   // font 의 크기
                            lineHeight = 10.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
                            fontWeight = FontWeight.Normal,  // font 의 굵기
                            style = MaterialTheme.typography.bodySmall,  //font 의 스타일
//                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
//                            modifier = Modifier
//                                .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                        )
                    }
                    Spacer(Modifier.size(10.dp))
                }
            }
        }
    },
    confirmButton = {
//        Row(
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            TextButton(
//                onClick = { onDismiss() }
//            ) {
//                Text(text = "확인", fontSize = 20.sp)
//            }
//        }
    },
    )
}
