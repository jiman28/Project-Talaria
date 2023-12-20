package com.example.projecttravel.ui.screens.boardwrite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.R
import com.example.projecttravel.ui.viewmodels.AddArticleUiState
import com.example.projecttravel.ui.viewmodels.BoardUiState
import com.example.projecttravel.ui.viewmodels.BoardViewModel
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import com.example.projecttravel.model.SendArticle
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.model.CallBoard

@Composable
fun WritePageButtons(
    title: String,
    content: String,
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    onBackButtonClicked: () -> Unit,
) {
    val tabTitle = stringResource(boardUiState.selectedWriteBoardMenu)

    val callBoard = CallBoard(
        kw = boardUiState.currentSearchKeyWord,
        page = boardUiState.currentBoardPage,
        type = stringResource(boardUiState.currentSearchType),
        email = boardUiState.currentSearchUser
    )

    val callCompany = CallBoard(
        kw = boardUiState.currentSearchKeyWord,
        page = boardUiState.currentCompanyPage,
        type = stringResource(boardUiState.currentSearchType),
        email = boardUiState.currentSearchUser
    )

    val callTrade = CallBoard(
        kw = boardUiState.currentSearchKeyWord,
        page = boardUiState.currentTradePage,
        type = stringResource(boardUiState.currentSearchType),
        email = boardUiState.currentSearchUser
    )

    val transformedContent = convertTextToHtml(content)
    val sendArticle = userPageUiState.currentLogin?.let {
        SendArticle(
            tabTitle = tabTitle,
            title = title,
            content = transformedContent,
            email = it.email,
        )
    }

    var isCancelWriteArticleDialog by remember { mutableStateOf(false) }
    var isAddArticleDialog by remember { mutableStateOf(false) }

    /** 로딩창 관리 ============================== */
    var txtErrorMsg by remember { mutableStateOf("") }
    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> TextMsgErrorDialog( txtErrorMsg = txtErrorMsg, onDismiss = { isLoadingState = null } )
            else -> isLoadingState = null
        }
    }

    var isTextErrorDialog by remember { mutableStateOf(false) }
    if (isTextErrorDialog) {
        TextMsgErrorDialog( txtErrorMsg = txtErrorMsg, onDismiss = { isTextErrorDialog = false },)
    }

    when (boardViewModel.addArticleUiState) {
        is AddArticleUiState.Loading -> isLoadingState = true
        is AddArticleUiState.Success -> {
            if ((boardViewModel.addArticleUiState as AddArticleUiState.Success).addArticle == true) {
                isLoadingState = null
                boardViewModel.getBoardList(callBoard)
                boardViewModel.getCompanyList(callCompany)
                boardViewModel.getTradeList(callTrade)
                boardViewModel.resetAddArticle()
                onBackButtonClicked()
            } else if ((boardViewModel.addArticleUiState as AddArticleUiState.Success).addArticle == false) {
                isLoadingState = false
                boardViewModel.resetAddArticle()
                txtErrorMsg = "게시글 저장 실패"
            }
        }
        else -> {
            isLoadingState = false
            txtErrorMsg = "게시글 저장 실패"
        }
    }

    /** UI 관리 ============================== */
    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
    ) {
        Row {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(1.dp),
                onClick = {
                    isCancelWriteArticleDialog = true
                },
                shape = RoundedCornerShape(0.dp),
            ) {
                Text(text = stringResource(R.string.cancel_button))
                if (isCancelWriteArticleDialog) {
                    CancelWriteArticleDialog(
                        boardViewModel = boardViewModel,
                        userPageViewModel = userPageViewModel,
                        onBackButtonClicked = onBackButtonClicked,
                        onDismiss = {
                            isCancelWriteArticleDialog = false
                        },
                    )
                }
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(1.dp),
                onClick = {
                    if (boardUiState.selectedWriteBoardMenu == R.string.selectTabTitle) {
                        txtErrorMsg = "게시판을 고르세요"
                        isTextErrorDialog = true
                    } else if (title == "") {
                        txtErrorMsg = "제목을 적으세요"
                        isTextErrorDialog = true
                    } else if (content == "") {
                        txtErrorMsg = "내용을 작성하세요"
                        isTextErrorDialog = true
                    } else {
                        isAddArticleDialog = true
                    }
                },
                shape = RoundedCornerShape(0.dp),
            ) {
                Text(text = stringResource(R.string.confirm_button))
                if (isAddArticleDialog) {
                    if (sendArticle != null) {
                        ArticleConfirmDialog(
                            sendArticle = sendArticle,
                            boardViewModel = boardViewModel,
                            onDismiss = {
                                isAddArticleDialog = false
                            },
                        )
                    }
                }
            }
        }
    }

    /** ================================================== */
    /** Bottom BackHandler Click Action ====================*/
    if (userPageUiState.isBackHandlerClick) {
        isCancelWriteArticleDialog = true
    }
}

fun convertTextToHtml(text: String): String {
    val lines = text.split("\n")
    val htmlLines = lines.map { "<p>$it</p>" }
    return htmlLines.joinToString("")
}

@Composable
fun ArticleConfirmDialog(
    sendArticle: SendArticle,
    boardViewModel: BoardViewModel,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "게시글을 저장하시겠습니까?",
                fontSize = 20.sp,
                lineHeight = 20.sp,
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
                        boardViewModel.addArticle(sendArticle)
                        onDismiss()
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
            }
        },
    )
}

@Composable
fun CancelWriteArticleDialog(
    boardViewModel: BoardViewModel,
    userPageViewModel: UserPageViewModel,
    onBackButtonClicked: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            userPageViewModel.setBackHandlerClick(false)
            onDismiss()
        },
        text = {
            Text(
                text = "작성을 취소하시겠습니까?\n지금까지 작성한 내용들은 저장되지 않습니다.",
                fontSize = 20.sp,
                lineHeight = 20.sp,
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
                        onDismiss()
                        boardViewModel.setWriteBoardMenu(R.string.selectTabTitle)
                        userPageViewModel.setBackHandlerClick(false)
                        onBackButtonClicked()
                    }
                ) {
                    Text(text = "확인", fontSize = 20.sp)
                }
                TextButton(
                    onClick = {
                        userPageViewModel.setBackHandlerClick(false)
                        onDismiss()
                    }
                ) {
                    Text(text = "취소", fontSize = 20.sp)
                }
            }
        },
    )
}
