package com.example.projecttravel.ui.screens.boardview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.R
import com.example.projecttravel.ui.viewmodels.AddCommentUiState
import com.example.projecttravel.ui.viewmodels.BoardUiState
import com.example.projecttravel.ui.viewmodels.BoardViewModel
import com.example.projecttravel.ui.viewmodels.RemoveCommentUiState
import com.example.projecttravel.ui.viewmodels.ReplyListUiState
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import com.example.projecttravel.model.RemoveComment
import com.example.projecttravel.model.SendComment
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.CallReply
import com.example.projecttravel.model.ReplyList
import com.example.projecttravel.ui.screens.GlobalLoadingScreen

@Composable
fun ShowReply(
    replyListUiState: ReplyListUiState,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    userPageUiState: UserPageUiState,
    currentArticleNo: Int,
) {
    when (replyListUiState) {
        is ReplyListUiState.Loading -> GlobalLoadingScreen()
        is ReplyListUiState.Success ->
            ViewReply(
                replyList = replyListUiState.replyList,
                boardUiState = boardUiState,
                boardViewModel = boardViewModel,
                userPageUiState = userPageUiState,
                currentArticleNo = currentArticleNo,
            )
        else -> NoArticlesFoundScreen()
    }
}

@Composable
fun ViewReply(
    replyList: List<ReplyList>,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    userPageUiState: UserPageUiState,
    currentArticleNo: Int,
) {
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
        TextMsgErrorDialog(
            txtErrorMsg = txtErrorMsg,
            onDismiss = {
                isTextErrorDialog = false
            },
        )
    }

    val tabtitle = stringResource(boardUiState.currentSelectedBoardTab)
    val callReply = CallReply(
        tabtitle = tabtitle,
        articleNo = currentArticleNo.toString()
    )
    when (boardViewModel.addCommentUiState) {
        is AddCommentUiState.Loading -> isLoadingState = true
        is AddCommentUiState.Success -> {
            if ((boardViewModel.addCommentUiState as AddCommentUiState.Success).addComment == true) {
                isLoadingState = null
                boardViewModel.getReplyList(callReply)
                boardViewModel.resetAddComment()
            } else if ((boardViewModel.addCommentUiState as AddCommentUiState.Success).addComment == false) {
                isLoadingState = false
                boardViewModel.resetAddComment()
                txtErrorMsg = "댓글 전송 실패"
            }
        }
        else -> {
            isLoadingState = false
            txtErrorMsg = "댓글 전송 실패"
        }
    }
    when (boardViewModel.removeCommentUiState) {
        is RemoveCommentUiState.Loading -> isLoadingState = true
        is RemoveCommentUiState.Success -> {
            if ((boardViewModel.removeCommentUiState as RemoveCommentUiState.Success).removeComment == true) {
                isLoadingState = null
                boardViewModel.getReplyList(callReply)
                boardViewModel.resetRemoveComment()
            } else if ((boardViewModel.removeCommentUiState as RemoveCommentUiState.Success).removeComment == false) {
                isLoadingState = false
                boardViewModel.resetRemoveComment()
                txtErrorMsg = "댓글 삭제 실패"
            }
        }
        else -> {
            isLoadingState = false
            txtErrorMsg = "댓글 삭제 실패"
        }
    }

    /** UI 관리 ============================== */
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(fontSize = 25.sp, text = stringResource(R.string.replyTitle))
            Spacer(modifier = Modifier.padding(3.dp))
            Text(fontSize = 18.sp, text = "(${replyList?.size ?: 0})") // 크기를 출력
        }
        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))
        Spacer(modifier = Modifier.padding(5.dp))
        if (replyList.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            ) {
                Text(fontSize = 15.sp, text = "댓글이 없네요\n첫 번째 댓글을 작성해주세요!")
            }
        } else {
            Column {
                replyList.forEach { reply ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                        horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                    ) {
                        Column(
                            modifier = Modifier.weight(8f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row {
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = Icons.Filled.AccountCircle,
                                        contentDescription = "Account"
                                    )
                                    Spacer(modifier = Modifier.padding(1.dp))
                                    Text(fontSize = 12.sp, text = reply.writeId)

                                    Spacer(modifier = Modifier.padding(5.dp))

                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = Icons.Filled.AccessTime,
                                        contentDescription = "WriteDate"
                                    )
                                    Spacer(modifier = Modifier.padding(1.dp))
                                    Text(fontSize = 12.sp, text = reply.writeDate.substring(0 until 10))
                                }
                                Row {
                                }
                            }
                            Column {
                                Text(
                                    modifier = Modifier.padding(start = 3.dp),
                                    fontSize = 15.sp,
                                    text = reply.replyContent
                                )
                            }
                        }
                        //                    Column(
                        //                        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                        //                        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                        //                        modifier = Modifier.weight(1f)
                        //                    ) {
                        //                        Icon(
                        //                            modifier = Modifier.size(15.dp),
                        //                            imageVector = Icons.Filled.Edit,
                        //                            contentDescription = "EditComment"
                        //                        )
                        //                    }
                        Column(
                            verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                            horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                            modifier = Modifier.weight(1f)
                        ) {
                            //                        userUiState.currentLogin?.id?.let { Text(text = "로그인 $it") }
                            //                        Text(text = "리플 ${reply.userId}")
                            var isRemoveCommentDialog by remember { mutableStateOf(false) }
                            if (userPageUiState.currentLogin?.id == reply.userId) {
                                Icon(
                                    modifier = Modifier
                                        .size(15.dp)
                                        .clickable {
                                            isRemoveCommentDialog = true
                                        },
                                    imageVector = Icons.Filled.Cancel,
                                    contentDescription = "CancelComment"
                                )
                            }
                            if (isRemoveCommentDialog) {
                                val removeComment = RemoveComment(
                                    tabTitle = tabtitle,
                                    articleNo = currentArticleNo.toString(),
                                    replyNo = reply.replyNo,
                                )
                                RemoveCommentDialog(
                                    removeComment = removeComment,
                                    boardViewModel = boardViewModel,
                                    onDismiss = {
                                        isRemoveCommentDialog = false
                                    },
                                )
                            }
                        }
                    }
                    Divider(thickness = dimensionResource(R.dimen.thickness_divider1))
                    Spacer(modifier = Modifier.padding(3.dp))
                }
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))
        Column {


            var commentContent by remember { mutableStateOf("") }
            var remainingCharacters by remember { mutableStateOf(100) } // 남은 문자 수를 나타내는 변수 추가

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(fontSize = 25.sp, text = "댓글 작성")
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.End, // 수평 가운데 정렬
                ) {
                    val sendComment = userPageUiState.currentLogin?.let {
                        SendComment(
                            tabTitle = tabtitle,
                            articleNo = currentArticleNo.toString(),
                            replyContent = commentContent,
                            email = it.email,
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                if (commentContent == "") {
                                    txtErrorMsg = "댓글을 작성하세요!"
                                    isTextErrorDialog = true
                                } else {
                                    if (sendComment != null) {
                                        boardViewModel.addComment(sendComment)
                                    }
                                }
                            },
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "EditComment"
                    )

                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    modifier = Modifier.weight(8f),
                ) {
                    val maxCharacters = 100
                    TextField(
                        value = commentContent,
                        onValueChange = {
                            // 문자 수 제한 로직 추가
                            if (it.length <= maxCharacters) {
                                commentContent = it
                                remainingCharacters = maxCharacters - it.length
                            }
                        },
                        label = { Text(text = "문자 수 제한: $remainingCharacters / 100") },
                        placeholder = { Text(text = "댓글을 작성해주세요.") },
                        trailingIcon = {  },
                        modifier = Modifier
                            .padding(16.dp)
                            .height(150.dp) // 높이 조절
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Default // 줄 바꿈 버튼 활성화
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun RemoveCommentDialog(
    removeComment: RemoveComment,
    boardViewModel: BoardViewModel,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "댓글을 삭제하시겠습니까?",
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
                        boardViewModel.removeComment(removeComment)
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
