package com.example.projecttravel.ui.screens.plantrip.plandialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.UserPageViewModel

/** ===================================================================== */
/** ResetPlanDialog to ask whether to select other plans or not ====================*/
@Composable
fun ResetPlanDialog(
    planPageViewModel: PlanPageViewModel,
    userPageViewModel: UserPageViewModel,
    onCancelButtonClicked: () -> Unit,  // 취소버튼 매개변수를 추가
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
            userPageViewModel.setBackHandlerClick(false)
        },
        text = {
            Text(
                text = "관광지를\n다시 고릅니다",
                fontSize = 20.sp,
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
                        planPageViewModel.resetAllPlanUiState()
                        userPageViewModel.setBackHandlerClick(false)
                        onCancelButtonClicked()
                        onDismiss()
                    }
                ) {
                    Text(text = "확인", fontSize = 20.sp)
                }
                TextButton(
                    onClick = {
                        onDismiss()
                        userPageViewModel.setBackHandlerClick(false)
                    }
                ) {
                    Text(text = "취소", fontSize = 20.sp)
                }
            }
        },
    )
}
