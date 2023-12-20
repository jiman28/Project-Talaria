package com.example.projecttravel.ui.screens.infome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.Board
import com.example.projecttravel.model.Company
import com.example.projecttravel.model.Trade
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
//import com.example.projecttravel.data.repositories.board.viewmodels.BoardUiState
//import com.example.projecttravel.data.repositories.board.viewmodels.CompanyUiState
//import com.example.projecttravel.data.repositories.board.viewmodels.TradeUiState
//import com.example.projecttravel.data.repositories.board.viewmodels.ListBoardRepoViewModel
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.ui.viewmodels.BoardViewModel

@Composable
fun MyInfoPage(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planUiState: PlanUiState,
    planPageViewModel: PlanPageViewModel,
    boardViewModel: BoardViewModel,
    boardUiState: com.example.projecttravel.ui.viewmodels.BoardUiState,
    navController: NavHostController,
    onBoardClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onResetButtonClicked: () -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp),
    ) {
        if (userPageUiState.checkOtherUser != null) {
            /** User Menus */
            Column (
            ) {
                Spacer(modifier = Modifier.padding(5.dp))
                /** ================================================== */
                /** UserInfos */
                Row {
                    Column {
                        UserProfiles(
                            currentUserInfo = userPageUiState.checkOtherUser,
                            userPageUiState = userPageUiState,
                            userPageViewModel = userPageViewModel,
                            boardViewModel = boardViewModel,
                            navController = navController,
                        )
                    }

                    Spacer(modifier = Modifier.padding(5.dp))
                    Column {

                    }
                }
                Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

                /** ================================================== */
                /** User Menus Tab Buttons*/
                Column {
                    MyInfoPageTabButtons(
                        userPageUiState = userPageUiState,
                        userPageViewModel = userPageViewModel
                    )
                }
                Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

                /** ================================================== */
                /** User Menus Tab Menus*/
                Column {
                    when (userPageUiState.currentSelectedUserTab) {
                        R.string.userTabMenuBoard -> {
                            UserWrittenBoard(
                                userPageUiState = userPageUiState,
                                planUiState = planUiState,
                                boardViewModel = boardViewModel,
                                boardUiState = boardUiState,
                                onResetButtonClicked = onResetButtonClicked,
                                onBoardClicked = onBoardClicked,
                            )
                        }

                        R.string.userTabMenuPlans -> {
                            UserPlanList(
                                userPageUiState = userPageUiState,
                                userPageViewModel = userPageViewModel,
                                planPageViewModel = planPageViewModel,
                                navController = navController,
                                onNextButtonClicked = onNextButtonClicked,
                            )
                        }
                    }
                }
            }
            /** ================================================== */
        } else {
            Text(text = "아이디 확인 과정에서 오류 발생\n다시 시도하길 바람", modifier = Modifier.padding(5.dp))
        }
    }
}

fun countAllBoardsOfUser(
    filteredBoardList: List<Board>?,
    filteredCompanyList: List<Company>?,
    filteredTradeList: List<Trade>?
): Int {
    val boardCount = filteredBoardList?.size ?: 0
    val companyCount = filteredCompanyList?.size ?: 0
    val tradeCount = filteredTradeList?.size ?: 0

    return boardCount + companyCount + tradeCount
}
