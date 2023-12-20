package com.example.projecttravel.ui.screens.infome

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.screens.GlobalErrorDialog
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TravelScreen
import com.example.projecttravel.ui.screens.infome.infodialog.DeletePlanDialog
import com.example.projecttravel.ui.screens.select.selectapi.getDateToWeather
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.screens.DefaultAppFontContent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun UserPlanList(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planPageViewModel: PlanPageViewModel,
    navController: NavHostController,
    onNextButtonClicked: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var isDeletePlanDialog by remember { mutableStateOf(false) }
    var selectedPlanIdToDelete by remember { mutableStateOf("") }

    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> GlobalErrorDialog(onDismiss = { isLoadingState = null })
            else -> isLoadingState = null
        }
    }

    if (userPageUiState.checkMyPlanList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            items(
                items = userPageUiState.checkMyPlanList,
                key = { userPlan -> userPlan.id }
            ) { userPlan ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            scope.launch {
                                isLoadingState = true
                                // 비동기 작업을 시작하고 결과(return)를 받아오기 위한 Deferred 객체를 생성합니다.
                                val weatherDeferred =
                                    async { getDateToWeather(userPlan, planPageViewModel) }
                                // Deferred 객체의 await() 함수를 사용하여 작업 완료를 대기하고 결과를 받아옵니다.
                                val isWeatherComplete = weatherDeferred.await()
                                // 모든 작업이 완료되었을 때만 실행합니다.
                                if (isWeatherComplete) {
                                    isLoadingState = null
                                    userPageViewModel.setCheckUserPlanDataPage(userPlan)
                                    userPageViewModel.setUserPlanDate(userPlan.startDay)
                                    onNextButtonClicked()
                                    onNextButtonClicked()
                                } else {
                                    isLoadingState = false
                                }
                            }
                        }
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
                                        .clip(RoundedCornerShape(10.dp)),
                                    model = ImageRequest.Builder(context = LocalContext.current)
                                        .data(userPlan.plans[0].list[0].img)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    error = painterResource(id = R.drawable.no_image_country),
                                    placeholder = painterResource(id = R.drawable.loading_img)
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(dimensionResource(R.dimen.padding_small))
                                        .background(
                                            color = Color(0xFFD4E3FF),
                                            shape = RoundedCornerShape(bottomEnd = 8.dp)
                                        )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(dimensionResource(R.dimen.padding_small))
                                    ) {
                                        Text(
                                            text = userPlan.planName,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                                            fontWeight = FontWeight.Thin,
                                            textAlign = TextAlign.Start,
                                        )
                                        Text(
                                            text = "${userPlan.startDay} ~ ${userPlan.endDay}",
                                            fontSize = 10.sp,   // font 의 크기
                                            style = MaterialTheme.typography.bodySmall,
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }

                            }

                            Column(
                                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFF3C5BA9),
                                        shape = RoundedCornerShape(topStart = 8.dp)
                                    )
                            ) {
                                if (userPageUiState.currentLogin?.email == userPlan.email) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                                        horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                                        modifier = Modifier.clickable {
                                            isDeletePlanDialog = true
                                            selectedPlanIdToDelete = userPlan.id
                                            Log.d("jiman=planId=DeletePlanlist", userPlan.id)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Cancel,
                                            contentDescription = "CancelTourAttraction",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .padding(dimensionResource(R.dimen.padding_small)),
                                        )
                                        Text(
                                            text = "삭제 하기",
                                            fontSize = 20.sp,   // font 의 크기
                                            fontWeight = FontWeight.Thin,  // font 의 굵기
                                            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                                            style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(dimensionResource(R.dimen.padding_small)),
                                        )
                                    }

                                }
                            }
                        }
                    }
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(10.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            AsyncImage(
//                                modifier = Modifier
//                                    .size(50.dp)
//                                    .clip(RoundedCornerShape(8.dp)),
//                                model = ImageRequest.Builder(context = LocalContext.current)
//                                    .data(userPlan.plans[0].list[0].img)
//                                    .crossfade(true)
//                                    .build(),
//                                contentDescription = null,
//                                contentScale = ContentScale.Crop,
//                                error = painterResource(id = R.drawable.no_image_country),
//                                placeholder = painterResource(id = R.drawable.loading_img)
//                            )
//                        }
//                        Column (
//                            modifier = Modifier.weight(3f)
//                        ) {
//                            Column {
//                                Text(
//                                    text = userPlan.planName,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(dimensionResource(R.dimen.padding_medium)),
//                                    style = MaterialTheme.typography.titleLarge,
//                                    fontWeight = FontWeight.Bold,
//                                    textAlign = TextAlign.Start
//                                )
//                            }
//                            Column {
//                                Text(
//                                    text = "${userPlan.startDay} ~ ${userPlan.endDay}",
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(dimensionResource(R.dimen.padding_medium)),
//                                    style = MaterialTheme.typography.bodySmall,
//                                    fontWeight = FontWeight.Bold,
//                                    textAlign = TextAlign.Start
//                                )
//                            }
//                        }
//                        Column (
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            if (userUiState.currentLogin?.email == userPlan.email) {
//                                IconButton(
//                                    modifier = Modifier
//                                        .padding(3.dp)
//                                        .clip(RoundedCornerShape(50.dp))
//                                        .background(Color.Blue),
//                                    onClick = {
//                                        isDeletePlanDialog = true
//                                        selectedPlanIdToDelete = userPlan.id
//                                        Log.d("jiman=planId=DeletePlanlist", userPlan.id)
//                                    }
//                                ) {
//                                    Icon(imageVector = Icons.Filled.Cancel, contentDescription = "CancelTourAttraction", tint = Color.White)
//
//                                }
//                            }
//                        }
//                    }
                }
            }
        }
    } else {
        NoPlansFoundScreen()
    }

    if (isDeletePlanDialog && selectedPlanIdToDelete != "") {
        DeletePlanDialog(
            planId = selectedPlanIdToDelete,
            userPageUiState = userPageUiState,
            userPageViewModel = userPageViewModel,
            onPlanDeleteClicked = { navController.navigate(TravelScreen.Page1A.name) },
            onDismiss = {
                isDeletePlanDialog = false
                selectedPlanIdToDelete = ""
            },
            onLoadingStarted = {
                isLoadingState = true
            },
            onErrorOccurred = {
                isLoadingState = false
                selectedPlanIdToDelete = ""
            },
        )
    }
}

// 계획에 글이 없을 시
@Composable
fun NoPlansFoundScreen(
) {
    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        modifier = Modifier.fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
    ) {
        Text(text = stringResource(R.string.noPlansFound))
    }
}
