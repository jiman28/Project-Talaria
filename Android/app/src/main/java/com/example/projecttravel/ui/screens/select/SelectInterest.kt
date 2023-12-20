package com.example.projecttravel.ui.screens.select

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Interests
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.model.CityInfo
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.model.InterestInfo

import com.example.projecttravel.ui.viewmodels.InterestUiState
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@Composable
fun SelectInterest(
    selectUiState: SelectUiState,
    interestUiState: InterestUiState.InterestSuccess,
    selectPageViewModel: SelectPageViewModel = viewModel(),
    selectedCountry: CountryInfo?,
    selectedCity: CityInfo?,
    selectedInterest: InterestInfo?,
) {
    InterestDropDownMenu(
        selectUiState = selectUiState,
        selectPageViewModel = selectPageViewModel,
        selectedCity = selectedCity,
        selectedCountry = selectedCountry,
        selectedInterest = selectedInterest,
        interestList = interestUiState.interestList,
    )
}

@Composable
fun InterestDropDownMenu(
    selectUiState: SelectUiState,
    selectPageViewModel: SelectPageViewModel = viewModel(),
    selectedCountry: CountryInfo?, // 선택된 나라 정보 받기
    selectedCity: CityInfo?, // 선택된 도시 정보 받기
    selectedInterest: InterestInfo?,
    interestList: List<InterestInfo>
) {
    // 1. DropDownMenu의 펼쳐짐 상태 정의
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    // 2. DropDownMenu의 Expanded 상태를 변경하기 위한 버튼 정의
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        onClick = { isDropDownMenuExpanded = true }
    ) {
        if (selectUiState.selectInterest != null) {
            Text(
                text = selectUiState.selectInterest.interestType,
                fontWeight = FontWeight.Thin,  // font 의 굵기
                fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
            )
        } else {
            Icon(imageVector = Icons.Filled.Interests, contentDescription = "Interests")
            Text(
                text = " 취향",
                fontWeight = FontWeight.Thin,  // font 의 굵기
                fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
            )
        }
    }

    // 3. DropDownMenu 정의
    DropdownMenu(
        modifier = Modifier.wrapContentSize(),
        expanded = isDropDownMenuExpanded,
        offset = DpOffset((-10).dp, 0.dp), // Dropdown Menu 의 위치 조정
        onDismissRequest = { isDropDownMenuExpanded = false }
    ) {
        if (selectedCountry == null) {
            AlertDialog(
                onDismissRequest = { isDropDownMenuExpanded = false },
                text = {
                    Text(
                        text = "나라를 선택하세요",
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                        modifier = Modifier
                            .padding(10.dp) // 원하는 여백을 추가).
                            .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isDropDownMenuExpanded = false
                        }
                    ) {
                        Text(text = "확인",fontSize = 20.sp,)
                    }
                }
            )
        } else if (selectedCity == null) {
            AlertDialog(
                onDismissRequest = { isDropDownMenuExpanded = false },
                text = {
                    Text(
                        text = "도시를 선택하세요",
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                        modifier = Modifier
                            .padding(10.dp) // 원하는 여백을 추가).
                            .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isDropDownMenuExpanded = false
                        }
                    ) {
                        Text(text = "확인",fontSize = 20.sp,)
                    }
                }
            )
        } else {
            // "모두 선택" 옵션 추가
            val showAllOption = selectedInterest == null
            Column(
                modifier = Modifier.selectable(
                    selected = showAllOption,
                    onClick = {
                        selectPageViewModel.setInterest(null) // 선택된 관심사를 null로 설정
                        isDropDownMenuExpanded = false // 메뉴 닫기
                    }
                )
            ) {
                Text(
                    text = "모두 선택",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                    modifier = Modifier
                        .padding(10.dp) // 원하는 여백을 추가).
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                )
            }
            // 나머지 관심사 목록 추가
            interestList.forEach { interest ->
                // "모두 선택" 옵션을 제외한 관심사만 표시
                Column(
                    modifier = Modifier.selectable(
                        selected = selectedInterest == interest,
                        onClick = {
                            selectPageViewModel.setInterest(interest) // 선택된 관심사를 null로 설정
                            isDropDownMenuExpanded = false // 메뉴 닫기
                        }
                    )
                ) {
                    Text(
                        text = interest.interestType,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                        modifier = Modifier
                            .padding(10.dp) // 원하는 여백을 추가).
                            .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
                    )
                }
            }
        }
    }
}