package com.example.projecttravel.ui.screens.boardview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projecttravel.R
import com.example.projecttravel.ui.viewmodels.BoardUiState
import com.example.projecttravel.ui.viewmodels.BoardViewModel

@Composable
fun BoardsPageTabButtons(
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
) {
    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
    ) {
        Row {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f).padding(1.dp),
                onClick = { boardViewModel.setBoardTab(R.string.boardTabTitle) },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    if (boardUiState.currentSelectedBoardTab == R.string.boardTabTitle) Color(0xFF005FAF) else Color.White,
                )
            ) {
                Text(
                    text = stringResource(R.string.boardTabTitle),
                    color = if (boardUiState.currentSelectedBoardTab == R.string.boardTabTitle) Color.White else Color(0xFF005FAF)
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .weight(1f).padding(1.dp),
                onClick = { boardViewModel.setBoardTab(R.string.companyTabTitle) },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    if (boardUiState.currentSelectedBoardTab == R.string.companyTabTitle) Color(0xFF005FAF) else Color.White,
                )
            ) {
                Text(
                    text = stringResource(R.string.companyTabTitle),
                    color = if (boardUiState.currentSelectedBoardTab == R.string.companyTabTitle) Color.White else Color(0xFF005FAF)
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .weight(1f).padding(1.dp),
                onClick = { boardViewModel.setBoardTab(R.string.tradeTabTitle) },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    if (boardUiState.currentSelectedBoardTab == R.string.tradeTabTitle) Color(0xFF005FAF) else Color.White,
                )
            ) {
                Text(
                    text = stringResource(R.string.tradeTabTitle),
                    color = if (boardUiState.currentSelectedBoardTab == R.string.tradeTabTitle) Color.White else Color(0xFF005FAF)
                )
            }
        }
    }
}
