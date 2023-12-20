package com.example.projecttravel.ui.screens.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.ui.viewmodels.CountryUiState
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalAirport
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@Composable
fun SelectCountry(
    selectUiState: SelectUiState,
    countryUiState: CountryUiState.CountrySuccess,
    selectPageViewModel: SelectPageViewModel,
    selectedCountry: CountryInfo?,
) {
    CountryDropDownMenu(
        selectUiState = selectUiState,
        selectPageViewModel = selectPageViewModel,
        selectedCountry = selectedCountry,
        countryList = countryUiState.countryList,
    )
}

/** for 문을 사용한 DropDownMenu List의  분할 */
@Composable
fun CountryDropDownMenu(
    selectUiState: SelectUiState,
    selectPageViewModel: SelectPageViewModel,
    selectedCountry: CountryInfo?, // 선택된 나라 정보 받기
    countryList: List<CountryInfo>,
) {
    // 1. DropDownMenu의 펼쳐짐 상태 정의
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    // 2. DropDownMenu의 Expanded 상태를 변경하기 위한 버튼 정의
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        onClick = { isDropDownMenuExpanded = true }
    ) {
        if (selectUiState.selectCountry != null) {
            Text(
                text = selectUiState.selectCountry.countryName,
                fontWeight = FontWeight.Thin,  // font 의 굵기
                fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
            )
        } else {
            Icon(imageVector = Icons.Filled.LocalAirport, contentDescription = "LocalAirport")
            Text(
                text = " 나라",
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
        offset = DpOffset(10.dp, 0.dp), // Dropdown Menu 의 위치 조정
        onDismissRequest = { isDropDownMenuExpanded = false }
    ) {
        if (countryList.isNotEmpty()) {
            Row {
                val groupSize = 10
                /** Number of Columns based by groupSize */
                for (groupIndex in 0 until (countryList.size + groupSize - 1) / groupSize) {
                    Column (
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    ) {
                        /** Number of Indexes for each Columns = groupSize */
                        for (index in groupIndex * groupSize until minOf((groupIndex + 1) * groupSize, countryList.size)) {
                            val country = countryList[index]
                            Row(
                                modifier = Modifier.selectable(
                                    selected = selectedCountry == country,
                                    onClick = {
                                        selectPageViewModel.setCountry(country) // 변경된 CountryInfo를 전달
                                        selectPageViewModel.setCity(null) // 나라를 변경하면 다른 값들 초기화
                                        selectPageViewModel.setInterest(null) // 나라를 변경하면 다른 값들 초기화
                                        isDropDownMenuExpanded = false // 메뉴 닫기
                                    }
                                ),
                            ) {
                                Text(
                                    text = country.countryName,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                                    modifier = Modifier
                                        .padding(3.dp) // 원하는 여백을 추가
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = "나라 목록이 비어 있습니다.\n인터넷 연결상태를 확인하세요",
                textAlign = TextAlign.Center,
                )
        }
    }
}
