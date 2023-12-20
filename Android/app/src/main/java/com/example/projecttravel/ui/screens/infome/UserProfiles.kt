package com.example.projecttravel.ui.screens.infome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.UserResponse
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import com.example.projecttravel.ui.screens.TravelScreen
import com.example.projecttravel.ui.screens.infome.infoapi.getPeopleLikeMe
import com.example.projecttravel.ui.screens.infome.infodialog.UserSimilarToMeDialog
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.screens.DefaultAppFontContent
import com.example.projecttravel.ui.viewmodels.BoardViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun UserProfiles(
    currentUserInfo: UserResponse,
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    boardViewModel: BoardViewModel,
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()

    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    var peopleErrorMsg by remember { mutableStateOf("") }
    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> TextMsgErrorDialog(
                txtErrorMsg = peopleErrorMsg,
                onDismiss = { isLoadingState = null })

            else -> isLoadingState = null
        }
    }

    var isMyHexaGraph by remember { mutableStateOf(false) }
    if (isMyHexaGraph) {
        userPageUiState.checkMyInterest?.let {
            MyInterestGraph(
                filteredInfoGraph = it,
                onDismiss = { isMyHexaGraph = false },
            )
        }
    }

    var isSimilarToMe by remember { mutableStateOf(false) }
    if (isSimilarToMe) {
        UserSimilarToMeDialog(
            userPageUiState = userPageUiState,
            userPageViewModel = userPageViewModel,
            boardViewModel = boardViewModel,
            onUserClicked = {
                isSimilarToMe = false
                navController.navigate(TravelScreen.Page1A.name)
            },
            onDismiss = { isSimilarToMe = false },
        )
    }


    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
    ) {
        //=====

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.drawable.banner_mypage),
                    contentDescription = "banner_mypage",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 5f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                    horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                        modifier = Modifier
                            .weight(1.5f)
                            .padding(1.dp)
                            .fillMaxWidth(),
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .border(
                                    BorderStroke(5.dp, Color(0xFF3C5BA9)),
                                    CircleShape
                                )
                                .size(100.dp)
                                .clip(RoundedCornerShape(50.dp)),
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(currentUserInfo.picture)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.icon_user),
                            placeholder = painterResource(id = R.drawable.loading_img)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                        horizontalAlignment = Alignment.Start, // 수평 가운데 정렬
                        modifier = Modifier
                            .weight(3.5f)
                            .padding(1.dp)
                            .fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "VerifiedUser"
                            )
                            Text(
                                text = currentUserInfo.name,
                                fontSize = 15.sp,   // font 의 크기
                                fontWeight = FontWeight.Bold,  // font 의 굵기
                                style = MaterialTheme.typography.titleMedium,  //font 의 스타일
                                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                        ) {
                            Icon(imageVector = Icons.Filled.Email, contentDescription = "Email")
                            Text(
                                text = currentUserInfo.email,
                                fontSize = 12.sp,   // font 의 크기
                                fontWeight = FontWeight.Bold,  // font 의 굵기
                                style = MaterialTheme.typography.titleMedium,  //font 의 스타일
                                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                            )
                        }
                    }
                }
            }
            if (userPageUiState.currentLogin?.id == currentUserInfo.id) {
                Image(
                    painter = painterResource(R.drawable.icon_edit),
                    contentDescription = "edit",
//                contentScale = ContentScale.None,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(30.dp)
                        .clickable {
                        navController.navigate(TravelScreen.Page1C.name)
                        },
                )
            }
        }

        //=====
        Column {
            Row {
//                TextButton(
//                    onClick = { userViewModel.setUserPageTab(R.string.userTabMenuBoard) },
//                ) {
//                    Text(text = "전체 게시글 개수 : $allBoardsCounts")
//                }
//                Spacer(modifier = Modifier.padding(10.dp))
                Column(
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    TextButton(
                        onClick = {
                            scope.launch {
                                isLoadingState = true
                                val peopleDeferred = async { getPeopleLikeMe(currentUserInfo) }
                                val peopleComplete = peopleDeferred.await()
                                if (peopleComplete.isNotEmpty()) {
                                    isLoadingState = null
                                    userPageViewModel.setSimilarUsers(peopleComplete)
                                    isSimilarToMe = true
                                } else {
                                    peopleErrorMsg = "서버 오류"
                                    isLoadingState = false
                                }
                            }
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.TagFaces, contentDescription = "TagFaces")
                        Text(
                            text = " 비슷한 유저",
                            fontSize = 18.sp,   // font 의 크기
                            fontWeight = FontWeight.Light,  // font 의 굵기
                            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    TextButton(
                        onClick = {
                            isMyHexaGraph = true
//                        navController.navigate(TravelScreen.Page1B.name)
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.AutoGraph, contentDescription = "AutoGraph")
                        Text(
                            text = " 성향 그래프",
                            fontSize = 18.sp,   // font 의 크기
                            fontWeight = FontWeight.Light,  // font 의 굵기
                            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        )
                    }
                }
            }
        }
    }

}