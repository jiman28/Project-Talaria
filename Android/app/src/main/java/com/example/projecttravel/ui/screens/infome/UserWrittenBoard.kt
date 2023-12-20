package com.example.projecttravel.ui.screens.infome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.ui.screens.boardview.BoardsPageTabButtons
import com.example.projecttravel.ui.screens.boardview.ShowListBoard
import com.example.projecttravel.ui.screens.boardview.ShowListCompany
import com.example.projecttravel.ui.screens.boardview.ShowListTrade
import com.example.projecttravel.ui.viewmodels.BoardUiState
import com.example.projecttravel.ui.viewmodels.BoardViewModel

@Composable
fun UserWrittenBoard(
    userPageUiState: UserPageUiState,
    planUiState: PlanUiState,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    onResetButtonClicked: () -> Unit,
    onBoardClicked: () -> Unit,
) {
    Column (
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
    ) {
        Column {
            BoardsPageTabButtons(
                boardViewModel = boardViewModel,
                boardUiState = boardUiState,
            )
        }

        Spacer(modifier = Modifier.padding(2.dp))
        /** Lists Show ====================*/
        Column {
            val currentUserMenuId = userPageUiState.checkOtherUser?.id
            if (currentUserMenuId != null) {
                // 필터링을 거친 리스트
                when (boardUiState.currentSelectedBoardTab) {
                    R.string.boardTabTitle -> {
                        ShowListBoard(
                            boardListUiState = boardViewModel.boardListUiState,
                            userPageUiState = userPageUiState,
                            boardViewModel = boardViewModel,
                            boardUiState = boardUiState,
                            onBoardClicked = onBoardClicked,
                        )
                    }

                    R.string.companyTabTitle -> {
                        ShowListCompany(
                            companyListUiState = boardViewModel.companyListUiState,
                            userPageUiState = userPageUiState,
                            boardViewModel = boardViewModel,
                            boardUiState = boardUiState,
                            onBoardClicked = onBoardClicked,
                        )
                    }

                    R.string.tradeTabTitle -> {
                        ShowListTrade(
                            tradeListUiState = boardViewModel.tradeListUiState,
                            userPageUiState = userPageUiState,
                            boardViewModel = boardViewModel,
                            boardUiState = boardUiState,
                            onBoardClicked = onBoardClicked,
                        )
                    }
                }
            }
        }
    }
}
