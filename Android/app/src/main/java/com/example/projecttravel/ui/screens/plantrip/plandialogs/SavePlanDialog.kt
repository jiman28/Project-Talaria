package com.example.projecttravel.ui.screens.plantrip.plandialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.PlansData
import com.example.projecttravel.ui.screens.plantrip.planapi.savePlanToMongoDb
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun SavePlanDialog(
    userPageUiState: UserPageUiState,
    planUiState: PlanUiState,
    planPageViewModel: PlanPageViewModel,
    selectPageViewModel: SelectPageViewModel,
    onPlanCompleteClicked: () -> Unit = {},
    onDismiss: () -> Unit,
    onLoadingStarted: () -> Unit,
    onErrorOccurred: () -> Unit,
) {
    var txtErrorMsg by remember { mutableStateOf("") }
    var isTextErrorDialog by remember { mutableStateOf(false) }

    var planTitle by remember { mutableStateOf("") }
    var remainingCharacters by remember { mutableStateOf(30) } // 남은 문자 수를 나타내는 변수 추가

    val scope = rememberCoroutineScope()

    val plansData = userPageUiState.currentLogin?.let {
        val selectedPlan = if (planUiState.weatherSwitch) {
            planUiState.dateToAttrByWeather
        } else {
            planUiState.dateToAttrByCity
        }
        PlansData(
            planName = planTitle,
            email = it.email,
            startDay = planUiState.planDateRange?.start.toString(),
            endDay = planUiState.planDateRange?.endInclusive.toString(),
            plans = selectedPlan
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column {
                Text(
                    text = "계획을 저장하시겠습니까?\n세부사항 조정은 웹페이지를 통해서 가능합니다.",
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                    modifier = Modifier
                        .padding(10.dp) // 원하는 여백을 추가).
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                )
                val maxCharacters = 30
                TextField(
                    value = planTitle,
                    onValueChange = {
                        // 문자 수 제한 로직 추가
                        if (it.length <= maxCharacters) {
                            planTitle = it
                            remainingCharacters = maxCharacters - it.length
                        }
                    },
                    label = { Text(text = "문자 수 제한: $remainingCharacters / 30") },
                    placeholder = { Text(text = "계획의 이름을 적어주세요!") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
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
                        if (planTitle == "") {
                            txtErrorMsg = "제목을 작성해주세요"
                            isTextErrorDialog = true
                        } else {
                            if (plansData != null) {
                                scope.launch {
                                    onLoadingStarted()
                                    // 비동기 작업을 시작하고 결과(return)를 받아오기 위한 Deferred 객체를 생성합니다.
                                    val planDeferred = async { savePlanToMongoDb(plansData) }

                                    // Deferred 객체의 await() 함수를 사용하여 작업 완료를 대기하고 결과를 받아옵니다.
                                    val isPlanComplete = planDeferred.await()
                                    // 모든 작업이 완료되었을 때만 실행합니다.
                                    if (isPlanComplete) {
                                        planPageViewModel.setWeatherSwitch(false)   // 날씨 버튼을 초기화 시켜줘야 한다
                                        selectPageViewModel.resetAllSelectUiState() // select 관련 창들을 전부 초기화
                                        onDismiss()
                                        onPlanCompleteClicked()
                                    } else {
                                        onDismiss()
                                        onErrorOccurred()
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Text(text = "확인", fontSize = 20.sp)
                }
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "취소", fontSize = 20.sp)
                }
                if (isTextErrorDialog) {
                    TextMsgErrorDialog(
                        txtErrorMsg = txtErrorMsg,
                        onDismiss = {
                            isTextErrorDialog = false
                        },
                    )
                }
            }
        },
    )
}
