package com.example.projecttravel.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projecttravel.R
import com.example.projecttravel.ui.viewmodels.BoardViewModel
import com.example.projecttravel.ui.screens.auth.InterestForm
import com.example.projecttravel.ui.screens.boardview.AllBoardsPage
import com.example.projecttravel.ui.screens.boardwrite.WriteArticlePage
import com.example.projecttravel.ui.screens.auth.LoginForm
import com.example.projecttravel.ui.screens.auth.SignInForm
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.screens.home.HomePage
import com.example.projecttravel.ui.screens.infomeplan.CheckMyPlanPage
import com.example.projecttravel.ui.screens.planroutegps.RouteGpsPage
import com.example.projecttravel.ui.screens.plantrip.PlanPage
import com.example.projecttravel.ui.screens.searchplace.SearchGpsPage
import com.example.projecttravel.ui.screens.select.SelectPage
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.SearchViewModel
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel
import com.example.projecttravel.ui.screens.boardview.ViewContentsBoard
import com.example.projecttravel.ui.screens.infome.MyInfoPage
import com.example.projecttravel.ui.screens.infomeedit.EditUserPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class TravelScreen(@StringRes val title: Int) {
    PageTest(title = R.string.pageTest),   // 각 화면의 제목 텍스트에 해당하는 각 열거형 케이스에 대한 리소스 값을 추가합
    PageTest2(title = R.string.pageTest2),   // 각 화면의 제목 텍스트에 해당하는 각 열거형 케이스에 대한 리소스 값을 추가합
    Page0(title = R.string.pageLogin),   // 각 화면의 제목 텍스트에 해당하는 각 열거형 케이스에 대한 리소스 값을 추가합
    Page0A(title = R.string.pageSignIn),
    Page0B(title = R.string.pageSignInComplete),
    Page1(title = R.string.page1),
    Page1A(title = R.string.pageUser),
    Page1B(title = R.string.pageUserPlans),
    Page1C(title = R.string.pageEditUser),
    Page2(title = R.string.page2),
    Page2A(title = R.string.pageGps),
    Page3(title = R.string.page3),
    Page3A(title = R.string.pageRoute),
    Page4(title = R.string.page4),
    Page4A(title = R.string.pageViewBoard),
    Page4B(title = R.string.pageWriteBoard),
}

/** Composable that displays screens */
@Composable
fun TravelScreenHome(
    userPageViewModel: UserPageViewModel = viewModel(),
    selectPageViewModel: SelectPageViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    planPageViewModel: PlanPageViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = TravelScreen.valueOf(backStackEntry?.destination?.route ?: TravelScreen.Page0.name)

    val userUiState by userPageViewModel.userPageUiState.collectAsState()
    val selectUiState by selectPageViewModel.selectUiState.collectAsState()
    val searchUiState by searchViewModel.searchUiState.collectAsState()
    val planUiState by planPageViewModel.planUiState.collectAsState()

    val boardViewModel: BoardViewModel = viewModel(factory = BoardViewModel.BoardFactory)
    val boardUiState by boardViewModel.boardUiState.collectAsState()

    /** State of topBar, set state to false on each currentScreens */
    var showTopBarAndDrawer by rememberSaveable { mutableStateOf(true)  }
    showTopBarAndDrawer = when (currentScreen) { // on this screens topBar should be hidden
        TravelScreen.Page0 -> false
        TravelScreen.Page0A -> false
        TravelScreen.Page1A -> false
        TravelScreen.Page1B -> false
        TravelScreen.Page1C -> false
        TravelScreen.Page0B -> false
        TravelScreen.Page2 -> false
        TravelScreen.Page2A -> false
        TravelScreen.Page3 -> false
        TravelScreen.Page3A -> false
        TravelScreen.Page4A -> false
        TravelScreen.Page4B -> false
        else -> true // in all other cases show bottom bar
    }

    /** All Screens => All contents */
    Scaffold(
        topBar = {
            /** shows TopBar only when true */
            if (showTopBarAndDrawer) {
                TravelAppBar(
                    currentScreen = currentScreen,
                    navController = navController,  // TravelApp()의 navController 를 전달
                    drawerState = drawerState,
                    scope = scope,
                )
            }
        }
    ) { innerPadding ->
        /** All pages ====================*/
        NavHost(    // NavHost 컴포저블을 추가
            navController = navController,
            startDestination = TravelScreen.Page0.name,
//            startDestination = TravelScreen.Page0B.name, // 화면 만들기 확인용
            modifier = Modifier.padding(innerPadding)
        ) {     // 최종 매개변수에 빈 후행 람다를 전달
            /** ============================================================ */
            /** 0. 로그인 페이지 ====================*/
            composable(route = TravelScreen.Page0.name) {
                LoginForm(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    onLoginSuccess = {
                        navController.navigate(TravelScreen.Page1.name)
                    },
                    onNextButtonClicked = {
                        navController.navigate(TravelScreen.Page0A.name)
                    },
                )
                /** Exit App when press BackHandler twice quickly */
                ExitAppWhenBackOnPressed(drawerState)
            }
            /** 0A. 회원 가입 화면 ====================*/
            composable(route = TravelScreen.Page0A.name) {
                SignInForm(
                    userPageViewModel = userPageViewModel,
                    onNextButtonClicked = {
                        navController.navigate(TravelScreen.Page0B.name)
                    },
                )
            }
            /** 0B. 회원 가입 성공 후 선호도 조사 ====================*/
            composable(route = TravelScreen.Page0B.name) {
                InterestForm(
                    userPageUiState = userUiState,
                    onCompleteButtonClicked = {
                        navController.navigate(TravelScreen.Page0.name)
                    },
                )
                BackHandler(
                    enabled = drawerState.isClosed,
                    onBack = { },    // 바로 전 페이지로 이동을 막아야함
                )
            }

            /** ============================================================ */
            /** 1. 홈페이지 ====================*/
            composable(route = TravelScreen.Page1.name) {
                /** ModalNavigationDrawer must always be placed before any screens */
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContents(
                            onLogOutClicked = {
                                navController.navigate(TravelScreen.Page0.name)
                            },
                            userPageUiState = userUiState,
                            userPageViewModel = userPageViewModel,
                            boardViewModel = boardViewModel,
                            boardUiState = boardUiState,
                            planPageViewModel = planPageViewModel,
                            navController = navController,
                            drawerState = drawerState,
                            scope = scope,
                        )
                    },
                ) {
                    HomePage(
                        userPageUiState = userUiState,
                        userPageViewModel = userPageViewModel,
                        onLogOutClicked = {
                            navController.navigate(TravelScreen.Page0.name)
                        },
                        onNextButtonClicked = {
                            navController.navigate(TravelScreen.Page2.name)
                        },
                        countryCardClicked = {
                            selectPageViewModel.setCountry(it) // 카드를 누르면 나라를 변경
                            selectPageViewModel.setCity(null) // 나라를 변경하면 다른 값들 초기화
                            selectPageViewModel.setInterest(null) // 나라를 변경하면 다른 값들 초기화
                            navController.navigate(TravelScreen.Page2.name)
                        },
                    )
                    /** Exit App when press BackHandler twice quickly */
                    ExitAppWhenBackOnPressed(drawerState)
                }
            }
            /** 1A. 내 정보 화면 (다른 유저 혼용) ====================*/
            composable(route = TravelScreen.Page1A.name) {
                MyInfoPage(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    planUiState = planUiState,
                    planPageViewModel = planPageViewModel,
                    boardViewModel = boardViewModel,
                    boardUiState = boardUiState,
                    navController = navController,
                    onBoardClicked = { navController.navigate(TravelScreen.Page4A.name) },
                    onNextButtonClicked = { navController.navigate(TravelScreen.Page1B.name) },
                    onResetButtonClicked = { navController.navigate(TravelScreen.Page1A.name) },
                )
                BackHandler(
                    enabled = drawerState.isClosed,
                    onBack = {
                        planPageViewModel.resetAllPlanUiState()
                        navController.navigate(TravelScreen.Page1.name)
                        userPageViewModel.previousScreenWasPageOneA(false)
                    },
                )
            }
            /** 1B. 내가 만든 계획 확인 (다른 유저 혼용) ====================*/
            composable(route = TravelScreen.Page1B.name) {
                CheckMyPlanPage(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    planUiState = planUiState,
                    planPageViewModel = planPageViewModel,
                    onBackButtonClicked = {
                        navController.navigate(TravelScreen.Page1A.name)
                    },
                    onRouteClicked = { navController.navigate(TravelScreen.Page3A.name) },
                )
                BackHandler(
                    onBack = { navController.navigate(TravelScreen.Page1A.name) },    // 바로 전 페이지로 이동
                )
            }

            /** 1C. 유저 정보 변경 ====================*/
            composable(route = TravelScreen.Page1C.name) {
                EditUserPage(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    navController = navController,
                )
                BackHandler(
                    onBack = { navController.navigate(TravelScreen.Page1A.name) },    // 바로 전 페이지로 이동
                )
            }

            /** ============================================================ */
            /** 2. 나라, 도시, 관광지 선택 화면 ====================*/
            composable(route = TravelScreen.Page2.name) {
                SelectPage(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    planPageViewModel = planPageViewModel,
                    selectUiState = selectUiState,
                    selectPageViewModel = selectPageViewModel, // 이 부분이 추가되어야 SelectPage 내에서 viewModel 코드가 돌아감!!!!!
                    onCancelButtonClicked = { navController.navigate(TravelScreen.Page1.name) },
                    onNextButtonClicked = { navController.navigate(TravelScreen.Page3.name) },
                    onGpsClicked = { navController.navigate(TravelScreen.Page2A.name) },
                )
                BackHandler(
                    onBack = { userPageViewModel.setBackHandlerClick(true) },
//                    onBack = { navController.navigate(TravelScreen.Page1.name) },
                )
            }
            /** 2-1. GPS 선택 화면 ====================*/
            composable(route = TravelScreen.Page2A.name) {
                SearchGpsPage(
                    selectUiState = selectUiState,
                    searchUiState = searchUiState,
                    searchViewModel = searchViewModel,
                    selectPageViewModel = selectPageViewModel,
                    onBackButtonClicked = { navController.navigate(TravelScreen.Page2.name) },
                    updateUiPageClicked = { navController.navigate(TravelScreen.Page2A.name) },
                )
                BackHandler(
                    onBack = { navController.navigate(TravelScreen.Page2.name) },
                )
            }

            /** ============================================================ */
            /** 3. 여행 플랜 짜기 화면 ====================*/
            composable(route = TravelScreen.Page3.name) {
                PlanPage(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    planUiState = planUiState,
                    planPageViewModel = planPageViewModel,
                    selectPageViewModel = selectPageViewModel,
                    onCancelButtonClicked = { navController.navigate(TravelScreen.Page2.name) },
                    onPlanCompleteClicked = { navController.navigate(TravelScreen.Page1.name) },
                    onRouteClicked = { navController.navigate(TravelScreen.Page3A.name) },
                )
                BackHandler(
                    onBack = { userPageViewModel.setBackHandlerClick(true) },
                )
            }
            /** 3-1. 경로 확인 화면 ====================*/
            composable(route = TravelScreen.Page3A.name) {
                RouteGpsPage(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    planUiState = planUiState,
                    planPageViewModel = planPageViewModel,
                    onBackButtonClicked = {
                        planPageViewModel.setGpsPage(null)
                        navController.navigateUp()
                    },
                )
                BackHandler(
                    enabled = drawerState.isClosed,
                    onBack = {
                        planPageViewModel.setGpsPage(null)
                        navController.navigateUp()
                    },
                )
            }

            /** ============================================================ */
            /** 4. 게시판 화면 ====================*/
            composable(route = TravelScreen.Page4.name) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContents(
                            onLogOutClicked = {
                                navController.navigate(TravelScreen.Page0.name)
                            },
                            userPageUiState = userUiState,
                            userPageViewModel = userPageViewModel,
                            boardViewModel = boardViewModel,
                            boardUiState = boardUiState,
                            planPageViewModel = planPageViewModel,
                            navController = navController,
                            drawerState = drawerState,
                            scope = scope,
                        )
                    },
                ) {
                    AllBoardsPage(
                        userPageUiState = userUiState,
                        boardViewModel = boardViewModel,
                        boardUiState = boardUiState,
                        onBoardClicked = { navController.navigate(TravelScreen.Page4A.name) },
                        onWriteButtonClicked = { navController.navigate(TravelScreen.Page4B.name) },
                    )
                    BackHandler(
                        enabled = drawerState.isClosed,
                        onBack = {
                            planPageViewModel.resetAllPlanUiState()
                            navController.navigate(TravelScreen.Page1.name)
                        },
                    )
                }
            }
            /** 4-1. 단일 게시판 보기 화면 ====================*/
            composable(route = TravelScreen.Page4A.name) {
                ViewContentsBoard(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    boardViewModel = boardViewModel,
                    boardUiState = boardUiState,
                    onBackButtonClicked = {
                        if (userUiState.previousScreenWasPageOneA) {
                            navController.navigate(TravelScreen.Page1A.name)
                        } else {
                            navController.navigate(TravelScreen.Page4.name)
                        }
                    },
                    onUserButtonClicked = { navController.navigate(TravelScreen.Page1A.name) }
                )
                BackHandler(
                    enabled = drawerState.isClosed,
                    onBack = {
                        if (userUiState.previousScreenWasPageOneA) {
                            navController.navigate(TravelScreen.Page1A.name)    // MyPage 에서 글을 보는 경우 Back 할 시 다시 MyPage 로 가야함.
                        } else {
                            navController.navigate(TravelScreen.Page4.name)    // MainBoard 에서 글을 보는 경우 Back 할 시 다시 MainBoard 로 가야함.
                        }
                    },
                )
            }
            /** 4-2. 게시판 작성 화면 ====================*/
            composable(route = TravelScreen.Page4B.name) {
                WriteArticlePage(
                    userPageUiState = userUiState,
                    userPageViewModel = userPageViewModel,
                    boardViewModel = boardViewModel,
                    boardUiState = boardUiState,
                    onBackButtonClicked = { navController.navigate(TravelScreen.Page4.name) },
                )
                BackHandler(
                    enabled = drawerState.isClosed,
                    onBack = { userPageViewModel.setBackHandlerClick(true) },
                )
            }
        }
        /** DrawerMenu Screen closed when click phone's backButton */
        /** Must be placed beneath NavHost() to apply this BackHandler logic to all pages */
        BackHandler(
            enabled = drawerState.isOpen,
            onBack = { scope.launch { drawerState.close() } }
        )
    }
}

/** Exit App when drawerState.isClosed && press BackHandler twice quickly */
@Composable
fun ExitAppWhenBackOnPressed(
    drawerState: DrawerState,
) {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    // drawerState 가 닫힌 상태일 때 backPressedState 이 작동 된다
    BackHandler(enabled = drawerState.isClosed && backPressedState) {
        if (System.currentTimeMillis() - backPressedTime <= 700L) {
            // 뒤로 가기를 한 번 누르고  700L (0.7초 = 700밀리초) 안으로 한 번 더 눌러야 종료됨
            (context as Activity).finish()
        } else {
            backPressedState = true
            // 뒤로 가기를 한 번 누르면 나오는 안내문
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}

@Composable
fun DefaultAppFontContent(): FontFamily {
    return FontFamily(Font(R.font.jua))
//    return FontFamily.Default
}