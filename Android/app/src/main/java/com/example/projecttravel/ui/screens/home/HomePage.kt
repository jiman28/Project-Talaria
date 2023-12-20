package com.example.projecttravel.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttravel.R
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.screens.LoginErrorDialog
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.viewmodels.HomepageRepoViewModel
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@Composable
fun HomePage(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    onLogOutClicked: () -> Unit,
    countryCardClicked: (CountryInfo) -> Unit,
    onNextButtonClicked: () -> Unit,    // 매개변수 추가
) {
    if (userPageUiState.currentLogin == null) {
        Surface {
            LoginErrorDialog(
                userPageViewModel = userPageViewModel,
                onLogOutClicked = onLogOutClicked
            )
        }
    }

    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.drawable.banner_homepage),
                    contentDescription = "banner_homepage",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 5f)
                        .clickable { onNextButtonClicked() }
                )
                Text(
                    text = "Let's go Trip ! !",
                    fontSize = 20.sp,   // font 의 크기
                    fontWeight = FontWeight.Light,  // font 의 굵기
                    fontFamily = FontFamily(Font(R.font.lobster)),  // font 의 글씨체(커스텀)
                    style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                    textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                    color = Color(0xFF0000FF)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        ) {
            Text(
                text = "나라의 사진을 누르면 나라 정보를 볼수 있어요",
                fontSize = 15.sp,   // font 의 크기
                fontWeight = FontWeight.Light,  // font 의 굵기
                fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                modifier = Modifier.padding(top = 5.dp)
            )
        }
        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))
        Column(
        ) {
            val homepageRepoViewModel: HomepageRepoViewModel = viewModel(factory = HomepageRepoViewModel.Factory)
            ShowCountryInfos(
                homepageUiState = homepageRepoViewModel.homepageUiState,
                countryCardClicked = countryCardClicked,
                retryAction = homepageRepoViewModel::getCountry,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
