package com.example.projecttravel.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttravel.R
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.ui.screens.GlobalErrorScreen
import com.example.projecttravel.ui.screens.GlobalLoadingScreen
import com.example.projecttravel.ui.viewmodels.HomepageUiState
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@Composable
fun ShowCountryInfos(
    homepageUiState: HomepageUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    countryCardClicked: (CountryInfo) -> Unit
) {
    when (homepageUiState) {
        is HomepageUiState.Loading -> GlobalLoadingScreen()
        is HomepageUiState.Success ->
            CountryListScreen(
                countryInfo = homepageUiState.countryInfo,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    ),
                countryCardClicked = countryCardClicked // countryCardClicked 함수를 전달
            )

        else -> GlobalErrorScreen(retryAction)
    }
}

@Composable
private fun CountryListScreen(
    countryInfo: List<CountryInfo>,
    modifier: Modifier = Modifier,
    countryCardClicked: (CountryInfo) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(
            items = countryInfo,
            key = { countryInfo ->
                countryInfo.countryName
            }
        ) { countryInfo ->
            TravelCard(countryInfo = countryInfo) {
                countryCardClicked(countryInfo)
            }
        }
    }
}

@Composable
fun TravelCard(
    countryInfo: CountryInfo,
    onClick: (CountryInfo) -> Unit,
) {
    var isCountryInfoDialogVisible by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = Color(0xFFD4E3FF))  // 색깔 설정
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_medium))
                            .size(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                isCountryInfoDialogVisible = true
                            },
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(countryInfo.imageC)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.no_image_country),
                        placeholder = painterResource(id = R.drawable.loading_img)
                    )
                    Column(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFD4E3FF),
                                shape = RoundedCornerShape(bottomEnd = 8.dp)
                            )
                    ) {
                        Text(
                            text = countryInfo.countryName,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.padding_small)),
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                            fontWeight = FontWeight.Thin,
                            textAlign = TextAlign.Start,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF3C5BA9),
                            shape = RoundedCornerShape(topStart = 8.dp)
                        )
                ) {
                    Text(
                        text = "${countryInfo.countryName}로 여행 가기  ",
                        fontSize = 20.sp,   // font 의 크기
                        fontWeight = FontWeight.Thin,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        color = Color.White,
                        modifier = Modifier.clickable { onClick(countryInfo) }.padding(dimensionResource(R.dimen.padding_small)),
                    )
                }
            }
        }
    }
    if (isCountryInfoDialogVisible) {
        CountryInfoDialog(
            countryInfo = countryInfo,
            onDismiss = {
                isCountryInfoDialogVisible = false
            }
        )
    }
}

/** The Pop-up Message Showing Country Information */
@Composable
fun CountryInfoDialog(
    countryInfo: CountryInfo,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column {
                Text(
                    text = countryInfo.countryName,
                    fontSize = 35.sp,   // font 의 크기
                    fontWeight = FontWeight.Light,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                    textDecoration = TextDecoration.Underline, // 텍스트 꾸미기
                    modifier = Modifier
                        .padding(10.dp) // 원하는 여백을 추가).
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                )
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(countryInfo.imageC)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.no_image_country),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
                Text(
                    text = (stringResource(R.string.title_colon,
                        "사용 언어",
                        countryInfo.languageC)),
                    fontSize = 20.sp,   // font 의 크기
                    fontWeight = FontWeight.Normal,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Justify, // 텍스트 내용을 compose 가운데 정렬
                    modifier = Modifier
                        .padding(10.dp) // 원하는 여백을 추가).
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                )

                Text(
                    text = (stringResource(R.string.title_colon,
                        "사용 화폐",
                        countryInfo.currency)),
                    fontSize = 20.sp,   // font 의 크기
                    fontWeight = FontWeight.Normal,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Justify, // 텍스트 내용을 compose 가운데 정렬
                    modifier = Modifier
                        .padding(10.dp) // 원하는 여백을 추가).
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                )

                Divider(thickness = dimensionResource(R.dimen.thickness_divider1))

                Text(
                    text = countryInfo.countryInfo,
                    fontSize = 20.sp,   // font 의 크기
                    fontWeight = FontWeight.Normal,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Justify, // 텍스트 내용을 compose 가운데 정렬
                    modifier = Modifier
                        .padding(10.dp) // 원하는 여백을 추가).
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = "확인",fontSize = 20.sp,)
            }
        },
    )
}
