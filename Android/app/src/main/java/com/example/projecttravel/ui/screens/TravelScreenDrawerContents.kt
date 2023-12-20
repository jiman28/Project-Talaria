package com.example.projecttravel.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttravel.R
import com.example.projecttravel.ui.viewmodels.BoardViewModel
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.model.CallBoard
import com.example.projecttravel.ui.screens.auth.datastore.DataStore
import com.example.projecttravel.ui.screens.auth.datastore.DataStore.Companion.dataStore
import com.example.projecttravel.ui.screens.infome.infoapi.callMyInterest
import com.example.projecttravel.ui.screens.infome.infoapi.callMyPlanList
import com.example.projecttravel.ui.viewmodels.BoardUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * ModalNavigationDrawer Contents
 * */
@Composable
fun DrawerContents(
    onLogOutClicked: () -> Unit,
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planPageViewModel: PlanPageViewModel,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    val context = LocalContext.current

    var isLogOutState by remember { mutableStateOf(false) }
    Surface {
        if (isLogOutState) {
            LogOutDialog(
                userPageViewModel = userPageViewModel,
                onDismissAlert = { isLogOutState = false },
                onLogOutClicked = onLogOutClicked
            )
        }
    }

    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> GlobalErrorDialog(onDismiss = { isLoadingState = null })
            else -> isLoadingState = null
        }
    }

    ModalDrawerSheet(
        modifier = Modifier.size(280.dp, 600.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
            modifier = Modifier.selectable(
                selected = false,
                onClick = {
                    userPageViewModel.previousScreenWasPageOneA(true)
                    userPageViewModel.setUserPageInfo(userPageUiState.currentLogin)
                    navController.navigate(TravelScreen.Page1A.name)
                    scope.launch { drawerState.close() }
                }
            )
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            if (userPageUiState.currentLogin?.picture != null) {
                AsyncImage(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(userPageUiState.currentLogin.picture)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.icon_user),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.icon_user),
                    contentDescription = "icon_user",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )

            }
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "${userPageUiState.currentLogin?.name} 님,\n환영합니다",
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.padding(2.dp))
        Divider(thickness = dimensionResource(R.dimen.thickness_divider1))
        Spacer(modifier = Modifier.padding(2.dp))

        if (userPageUiState.currentLogin != null) {
            val callBoardMe = CallBoard(
                kw = "",
                page = 0,
                type = stringResource(boardUiState.currentSearchType),
                email = userPageUiState.currentLogin.email
            )
            val userResponse = userPageUiState.currentLogin
            Row(
                verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Image(
                    painter = painterResource(R.drawable.icon_mypage),
                    contentDescription = "icon_mypage",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.padding(2.dp))
                TextButton(onClick = {
                    scope.launch {
                        drawerState.close()
                        boardViewModel.resetBoardPage()
                        boardViewModel.getBoardList(callBoardMe)
                        boardViewModel.getCompanyList(callBoardMe)
                        boardViewModel.getTradeList(callBoardMe)

                        isLoadingState = true
                        val isDeferredInterest = async { callMyInterest(userResponse) }
                        val isDeferredPlan = async { callMyPlanList(userResponse) }
                        val isCompleteInterest = isDeferredInterest.await()
                        val isCompletePlan = isDeferredPlan.await()
                        // 모든 작업이 완료되었을 때만 실행합니다.
                        if (isCompleteInterest != null) {
                            isLoadingState = null
                            userPageViewModel.setUserInterest(isCompleteInterest)
                            userPageViewModel.setUserPlanList(isCompletePlan)
                            userPageViewModel.previousScreenWasPageOneA(true)
                            userPageViewModel.setUserPageInfo(userPageUiState.currentLogin)
                            navController.navigate(TravelScreen.Page1A.name)
                        } else {
                            isLoadingState = false
                        }
                    }
                }) {
                    Text(
                        text = "MY PAGE",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.titleLarge,  //font 의 스타일)
                    )
                }
            }
        } else {
            Text(text = "로그인 오류", fontSize = 25.sp)
        }

        Spacer(modifier = Modifier.padding(2.dp))
        Divider(thickness = dimensionResource(R.dimen.thickness_divider1))
        Spacer(modifier = Modifier.padding(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                painter = painterResource(R.drawable.icon_logout),
                contentDescription = "icon_logout",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.padding(2.dp))
            TextButton(onClick = {
                isLogOutState = true
                scope.launch {
                    drawerState.close()
                }
            }) {
                Text(
                    text = "LOGOUT",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    style = MaterialTheme.typography.titleLarge,  //font 의 스타일)
                )
            }
        }

        Spacer(modifier = Modifier.padding(2.dp))
        Divider(thickness = dimensionResource(R.dimen.thickness_divider1))
        Spacer(modifier = Modifier.padding(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                painter = painterResource(R.drawable.talaria),
                contentDescription = "talaria",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.padding(2.dp))
            TextButton(onClick = {
                userPageViewModel.previousScreenWasPageOneA(false)
                navController.navigate(TravelScreen.Page2.name)
                scope.launch { drawerState.close() }
            }) {
                Text(
                    text = "PLAN YOUR TRIP",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    style = MaterialTheme.typography.titleLarge,  //font 의 스타일)
                )
            }
        }

        Spacer(modifier = Modifier.padding(2.dp))
        Divider(thickness = dimensionResource(R.dimen.thickness_divider1))
        Spacer(modifier = Modifier.padding(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                painter = painterResource(R.drawable.icon_board),
                contentDescription = "icon_board",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.padding(2.dp))
            val callBoardBoard = CallBoard(
                kw = "",
                page = 0,
                type = stringResource(boardUiState.currentSearchType),
                email = ""
            )
            TextButton(onClick = {
                scope.launch {
                    drawerState.close()
                    boardViewModel.resetBoardPage()
                    boardViewModel.getBoardList(callBoardBoard)
                    boardViewModel.getCompanyList(callBoardBoard)
                    boardViewModel.getTradeList(callBoardBoard)
                    navController.navigate(TravelScreen.Page4.name)
                }
            }) {
                Text(
                    text = "BOARD",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,  // font 의 굵기
                    fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                    style = MaterialTheme.typography.titleLarge,  //font 의 스타일)
                )
            }
        }

        Spacer(modifier = Modifier.padding(2.dp))
        Divider(thickness = dimensionResource(R.dimen.thickness_divider1))
        Spacer(modifier = Modifier.padding(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                painter = painterResource(R.drawable.logo_google_maps),
                contentDescription = "logo_google_maps",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable {
                        scope.launch {
                            drawerState.close()
                            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0"))
                            // Check if there's a mapping app available before starting the activity
                            context.startActivity(mapIntent)
                        }
                    }
            )
            Spacer(modifier = Modifier.padding(2.dp))
        }

        Spacer(modifier = Modifier.padding(2.dp))
        Divider(thickness = dimensionResource(R.dimen.thickness_divider1))
        Spacer(modifier = Modifier.padding(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                painter = painterResource(R.drawable.logo_skyscanner),
                contentDescription = "logo_skyscanner",
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            drawerState.close()
                            val url = "https://www.skyscanner.co.kr/"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            // Check if there's a web browser available before starting the activity
                            context.startActivity(intent)
                        }
                    }
            )
            Spacer(modifier = Modifier.padding(2.dp))
        }
    }
}

/** ===================================================================== */
/** LogOutDialog to ask whether to logout or not ====================*/
@Composable
fun LogOutDialog(
    userPageViewModel: UserPageViewModel,
    onLogOutClicked: () -> Unit,
    onDismissAlert: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = (context).dataStore
    AlertDialog(
        onDismissRequest = {
            onDismissAlert()
        },
        text = {
            Text(
                text = "로그아웃\n하시겠습니까?",
                fontSize = 30.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                modifier = Modifier
                    .padding(10.dp) // 원하는 여백을 추가).
                    .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
            )
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            userPageViewModel.resetUser()
                            dataStore.edit { preferences ->
                                preferences[DataStore.emailKey] = ""
                                preferences[DataStore.pwdKey] = ""
                            }
                            onLogOutClicked()
                        }
                    }
                ) {
                    Text(text = "확인", fontSize = 20.sp)
                }
                TextButton(
                    onClick = {
                        onDismissAlert()
                    }
                ) {
                    Text(text = "취소", fontSize = 20.sp)
                }
            }
        },
    )
}
