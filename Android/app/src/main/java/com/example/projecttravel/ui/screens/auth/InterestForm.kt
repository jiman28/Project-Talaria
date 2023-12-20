package com.example.projecttravel.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.SendInterest
import com.example.projecttravel.ui.screens.DefaultAppFontContent
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import com.example.projecttravel.ui.screens.auth.api.interestSaveApiCall
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun InterestForm(
    userPageUiState: UserPageUiState,
    onCompleteButtonClicked: () -> Unit,
) {
    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    var interestErrorMsg by remember { mutableStateOf("") }

    var sightsPosition by remember { mutableFloatStateOf(5F) }
    var naturePosition by remember { mutableFloatStateOf(5F) }
    var culturePosition by remember { mutableFloatStateOf(5F) }
    var historyPosition by remember { mutableFloatStateOf(5F) }
    var foodPosition by remember { mutableFloatStateOf(5F) }
    var religionPosition by remember { mutableFloatStateOf(5F) }

    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> TextMsgErrorDialog(
                txtErrorMsg = interestErrorMsg,
                onDismiss = { isLoadingState = null })

            else -> isLoadingState = null
        }
    }

    Column (
        verticalArrangement = Arrangement.Top, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(start = 15.dp, end = 15.dp),
    ) {

        Spacer(modifier = Modifier.height(20.dp))

//        Text(
//            text = "자신의 취향을 고르세요",
//            fontSize = 20.sp,   // font 의 크기
//            lineHeight = 20.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
//            fontWeight = FontWeight.ExtraBold,  // font 의 굵기
//            style = MaterialTheme.typography.titleLarge,  //font 의 스타일
//            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
//            modifier = Modifier
//                .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
//        )

        Text(
            text = "Select Your Interest !",
            fontSize = 30.sp,   // font 의 크기
            fontWeight = FontWeight.Light,  // font 의 굵기
            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
            style = MaterialTheme.typography.titleLarge,  //font 의 스타일
            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
            color = Color(0xFF0000FF),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        //sightsPosition
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier.background(
                color = Color(0xFFDAE1FF),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Slider(
                value = sightsPosition,
                onValueChange = { sightsPosition = it },
                steps = 9,
                valueRange = 0f..10F
            )
            Text(text = "명소 = ${sightsPosition.toInt()} point")
        }

        Spacer(modifier = Modifier.height(20.dp))

        //naturePosition
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier.background(
                color = Color(0xFFDAE1FF),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Slider(
                value = naturePosition,
                onValueChange = { naturePosition = it },
                steps = 9,
                valueRange = 0f..10F
            )
            Text(text = "자연 = ${naturePosition.toInt()} point")
        }

        Spacer(modifier = Modifier.height(20.dp))

        //culturePosition
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier.background(
                color = Color(0xFFDAE1FF),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Slider(
                value = culturePosition,
                onValueChange = { culturePosition = it },
                steps = 9,
                valueRange = 0f..10F
            )
            Text(text = "문화 = ${culturePosition.toInt()} point")
        }

        Spacer(modifier = Modifier.height(20.dp))

        //historyPosition
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier.background(
                color = Color(0xFFDAE1FF),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Slider(
                value = historyPosition,
                onValueChange = { historyPosition = it },
                steps = 9,
                valueRange = 0f..10F
            )
            Text(text = "역사 = ${historyPosition.toInt()} point")
        }

        Spacer(modifier = Modifier.height(20.dp))

        //foodPosition
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier.background(
                color = Color(0xFFDAE1FF),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Slider(
                value = foodPosition,
                onValueChange = { foodPosition = it },
                steps = 9,
                valueRange = 0f..10F
            )
            Text(text = "음식 = ${foodPosition.toInt()} point")
        }

        Spacer(modifier = Modifier.height(20.dp))

        //religionPosition
        Column(
            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            modifier = Modifier.background(
                color = Color(0xFFDAE1FF),
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Slider(
                value = religionPosition,
                onValueChange = { religionPosition = it },
                steps = 9,
                valueRange = 0f..10F
            )
            Text(text = "종교 = ${religionPosition.toInt()} point")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoadingState = true
                    if (userPageUiState.currentSignIn!= null) {
                        val sendInterest = SendInterest(
                            email = userPageUiState.currentSignIn.email,
                            sights = sightsPosition.toInt().toString(),
                            nature = naturePosition.toInt().toString(),
                            culture = culturePosition.toInt().toString(),
                            history = historyPosition.toInt().toString(),
                            food = foodPosition.toInt().toString(),
                            religion = religionPosition.toInt().toString(),
                            )
                        val interestDeferred =
                            async { interestSaveApiCall(sendInterest, userPageUiState) }
                        val interestComplete = interestDeferred.await()

                        if (interestComplete) {
                            isLoadingState = null
                            onCompleteButtonClicked()
                        } else {
                            interestErrorMsg = "저장 실패\n다시 시도해 주세요."
                            isLoadingState = false
                        }
                    }
                }
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("선호도 저장하기!")
        }
    }
}
