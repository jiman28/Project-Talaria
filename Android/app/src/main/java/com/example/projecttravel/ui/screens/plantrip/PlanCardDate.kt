package com.example.projecttravel.ui.screens.plantrip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttravel.R
import com.example.projecttravel.model.WeatherResponseGet
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import java.time.LocalDate

@Composable
fun PlanCardDate(
    date: LocalDate,
    size: Int,
    planPageViewModel: PlanPageViewModel,
    weatherResponseGet: WeatherResponseGet?,
    onClick: (LocalDate) -> Unit, // 클릭 시 호출될 콜백 함수
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                onClick(date) // 클릭 시 해당 날짜를 콜백 함수로 전달
                planPageViewModel.setCurrentPlanDate(date)
            },
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .width(120.dp)
                .height(160.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 날짜 표시
            Column (
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).padding(1.dp),
            ) {
                Text(
                    text = date.toString(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(3.dp)
                )
            }
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(2f).padding(1.dp),
            ) {
                if (weatherResponseGet != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(
                                weatherResponseGet.icon
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.no_image_country),
                        placeholder = painterResource(id = R.drawable.loading_img)
                    )
                } else {
                    Text(
                        text = "데이터 없음",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
            Column (
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).padding(1.dp),
            ) {
                Text(
                    text = "관광지 : $size 개",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(3.dp)
                )
            }
        }
    }
}
