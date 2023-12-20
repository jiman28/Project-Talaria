package com.example.projecttravel.ui.screens.infome.infodialog

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.screens.infome.infoapi.callMyPlanList
import com.example.projecttravel.ui.screens.plantrip.planapi.deletePlanfromMongoDb
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun DeletePlanDialog(
    planId: String,
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    onPlanDeleteClicked: () -> Unit = {},
    onDismiss: () -> Unit,
    onLoadingStarted: () -> Unit,
    onErrorOccurred: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Log.d("jiman=planId=DeletePlanDialog", planId)
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column {
                Text(
                    text = "계획을\n삭제하시겠습니까?",
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                    modifier = Modifier
                        .padding(10.dp) // 원하는 여백을 추가).
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            onLoadingStarted()
                            // 비동기 작업을 시작하고 결과(return)를 받아오기 위한 Deferred 객체를 생성합니다.
                            val planDeferred = async { deletePlanfromMongoDb(planId) }
                            // Deferred 객체의 await() 함수를 사용하여 작업 완료를 대기하고 결과를 받아옵니다.
                            val isPlanDeleteComplete = planDeferred.await()
                            // 모든 작업이 완료되었을 때만 실행합니다.
                            if (isPlanDeleteComplete) {
                                val isDeferredPlan = async { userPageUiState.checkOtherUser?.let { callMyPlanList(it) } }
                                val isCompletePlan = isDeferredPlan.await()
                                if (isCompletePlan != null ) {
                                    userPageViewModel.setUserPlanList(isCompletePlan)
                                    userPageViewModel.previousScreenWasPageOneA(true)
                                    onDismiss()
                                    onPlanDeleteClicked()
                                }
                            } else {
                                onDismiss()
                                onErrorOccurred()
                            }
                        }
                    }
                ) {
                    Text(text = "확인", fontSize = 20.sp)
                }
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text(text = "취소", fontSize = 20.sp)
                }
            }
        },
    )
}
