package com.example.projecttravel.ui.screens.boardwrite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.projecttravel.R
import com.example.projecttravel.ui.viewmodels.BoardUiState
import com.example.projecttravel.ui.viewmodels.BoardViewModel
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel

@Composable
fun WriteArticlePage(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    onBackButtonClicked: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var remainingCharacters by remember { mutableStateOf(30) } // 남은 문자 수를 나타내는 변수 추가
    var content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(start = 15.dp, end = 15.dp),
    ) {
        Column {
            WritePageButtons(
                title = title,
                content = content,
                userPageUiState = userPageUiState,
                userPageViewModel = userPageViewModel,
                boardViewModel = boardViewModel,
                boardUiState = boardUiState,
                onBackButtonClicked = onBackButtonClicked
            )
        }

        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

        Column {
            Text(text = "게시글 작성")
        }

        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        ) {
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Text(text = "게시판 선택")
            }
            Column(
                modifier = Modifier.weight(8f)
            ) {
                BoardDropDownMenu(
                    boardViewModel = boardViewModel,
                    boardUiState = boardUiState,
                )
            }
        }

        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
        ) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(text = "제목")
            }
            Column(
                modifier = Modifier.weight(8f)
            ) {
                val maxCharacters = 30
                TextField(
                    value = title,
                    onValueChange = {
                        // 문자 수 제한 로직 추가
                        if (it.length <= maxCharacters) {
                            title = it
                            remainingCharacters = maxCharacters - it.length
                        }
                    },
                    label = { Text(text = "문자 수 제한: $remainingCharacters / 30") },
                    maxLines = 2,
                    placeholder = { Text(text = "제목을 작성해주세요.") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next // 다음 버튼 활성화
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) } // 다음 버튼 클릭시 다음 TextField로 커서 이동
                    ),
                )
            }
        }

        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

        Column {
            Column {
                Text(text = "내용 작성")
            }
            Column {
                TextField(
                    value = content,
                    onValueChange = { newValue -> content = newValue },
                    label = { },
                    singleLine = false,
                    placeholder = { Text(text = "내용을 작성해주세요.") },
                    modifier = Modifier
                        .padding(16.dp)
//                        .verticalScroll(scrollState)
                        .height(400.dp) // 높이 조절
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default // 줄 바꿈 버튼 활성화
                    ),
                )
                Spacer(modifier = Modifier.padding(150.dp))
            }
        }
    }
}
