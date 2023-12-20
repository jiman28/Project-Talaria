package com.example.projecttravel.ui.screens.infome

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
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel

@Composable
fun MyInfoPageTabButtons(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
) {
    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
    ) {
        Row {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f).padding(1.dp),
                onClick = { userPageViewModel.setUserPageTab(R.string.userTabMenuBoard) },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    if (userPageUiState.currentSelectedUserTab == R.string.userTabMenuBoard) Color(0xFF005FAF) else Color.White,
                )
            ) {
                Text(
                    text = stringResource(R.string.userTabMenuBoard),
                    color = if (userPageUiState.currentSelectedUserTab == R.string.userTabMenuBoard) Color.White else Color(0xFF005FAF)
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .weight(1f).padding(1.dp),
                onClick = { userPageViewModel.setUserPageTab(R.string.userTabMenuPlans) },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    if (userPageUiState.currentSelectedUserTab == R.string.userTabMenuPlans) Color(0xFF005FAF) else Color.White,
                )
            ) {
                Text(
                    text = stringResource(R.string.userTabMenuPlans),
                    color = if (userPageUiState.currentSelectedUserTab == R.string.userTabMenuPlans) Color.White else Color(0xFF005FAF)
                    )
            }
        }
    }
}
