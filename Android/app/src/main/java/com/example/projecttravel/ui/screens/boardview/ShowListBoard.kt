package com.example.projecttravel.ui.screens.boardview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttravel.R
import com.example.projecttravel.ui.viewmodels.BoardListUiState
import com.example.projecttravel.ui.viewmodels.BoardUiState
import com.example.projecttravel.ui.viewmodels.BoardViewModel
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.BoardList
import com.example.projecttravel.model.CallBoard
import com.example.projecttravel.model.CallReply
import com.example.projecttravel.ui.screens.GlobalLoadingScreen

@Composable
fun ShowListBoard(
    boardListUiState: BoardListUiState,
    userPageUiState: UserPageUiState,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    onBoardClicked: () -> Unit,
) {
    when (boardListUiState) {
        is BoardListUiState.Loading -> GlobalLoadingScreen()
        is BoardListUiState.Success ->
            if (boardListUiState.boardList?.list?.isNotEmpty() == true) {
                ListBoardEntity(
                    boardList = boardListUiState.boardList,
                    userPageUiState = userPageUiState,
                    boardViewModel = boardViewModel,
                    boardUiState = boardUiState,
                    onBoardClicked = onBoardClicked,
                )
            } else {
                NoArticlesFoundScreen()
            }
        else -> NoArticlesFoundScreen()
    }
}

@Composable
fun ListBoardEntity(
    boardList: BoardList,
    userPageUiState: UserPageUiState,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    onBoardClicked: () -> Unit,
) {
    Spacer(modifier = Modifier.padding(5.dp))
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        items(
            items = boardList.list,
            key = { board ->
                board.articleNo
            }
        ) { board ->
            Card(
                modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                shape = RoundedCornerShape(5.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                    horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = board.articleNo.toString())
                    }
                    Column(
                        modifier = Modifier.weight(7f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                        ) {
                            Column(
                                modifier = Modifier.weight(8f)
                            ) {
                                val tabtitle = stringResource(boardUiState.currentSelectedBoardTab)
                                val callReply = CallReply(
                                    tabtitle = tabtitle,
                                    articleNo = board.articleNo.toString()
                                )
                                TextButton(
                                    onClick = {
                                        boardViewModel.getReplyList(callReply)
                                        boardViewModel.setViewBoard(board)
                                        boardViewModel.setViewCounter(tabtitle, board.articleNo.toString())
                                        onBoardClicked()
                                    }
                                ) {
                                    EllipsisTextBoard(
                                        text = board.title,
                                        maxLength = 10,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.Bottom, // 수직 가운데 정렬
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(2f),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.Bottom, // 수직 가운데 정렬
                                    horizontalArrangement = Arrangement.End, // 수평 가운데 정렬
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val replyListCounter =
                                        boardList.replyCount.find { it.articleNo == board.articleNo }
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = Icons.Filled.Comment,
                                        contentDescription = "comments"
                                    )
                                    Spacer(modifier = Modifier.padding(2.dp))
                                    Text(
                                        fontSize = 15.sp,
                                        text = "${replyListCounter?.replyCount ?: 0}"
                                    ) // 크기를 출력
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Spacer(modifier = Modifier.padding(5.dp))
                                val userImage = board.user.picture
                                if (userImage != null) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(15.dp)
                                            .clip(RoundedCornerShape(50.dp)),
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(userImage)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        error = painterResource(id = R.drawable.icon_user),
                                        placeholder = painterResource(id = R.drawable.loading_img)
                                    )
                                } else {
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = Icons.Filled.AccountCircle,
                                        contentDescription = "Account"
                                    )
                                }
                                Spacer(modifier = Modifier.padding(2.dp))
                                Text(fontSize = 12.sp, text = board.writeId)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(
                                    modifier = Modifier.size(15.dp),
                                    imageVector = Icons.Filled.AccessTime,
                                    contentDescription = "WriteDate"
                                )
                                Spacer(modifier = Modifier.padding(2.dp))
                                Text(fontSize = 12.sp, text = board.writeDate.substring(0 until 10))

                                Spacer(modifier = Modifier.padding(5.dp))

                                Icon(
                                    modifier = Modifier.size(15.dp),
                                    imageVector = Icons.Filled.RemoveRedEye,
                                    contentDescription = "Views"
                                )
                                Spacer(modifier = Modifier.padding(2.dp))
                                Text(fontSize = 12.sp, text = board.views.toString())
                            }
                        }
                    }
                }
            }
        }
        item {
            Column(
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow {
                    items(boardList.pages) { index ->
                        // (index = boardList.pages - 1)까지의 값을 가지게 됩니다.
                        val callBoard = CallBoard(
                            kw = boardUiState.currentSearchKeyWord,
                            page = index,
                            type = stringResource(boardUiState.currentSearchType),
                            email = if (userPageUiState.checkOtherUser != null) userPageUiState.checkOtherUser.email else ""
                        )
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .background(
                                    if (boardUiState.currentBoardPage == index) Color(0xFF005FAF) else Color.White
                                )
                                .clickable {
                                    boardViewModel.getBoardList(callBoard)
                                    boardViewModel.setBoardPage(index)
                                }
                        ) {
                            Text(
                                text = (index + 1).toString(), // 1부터 시작하도록 표시
                                color = if (boardUiState.currentBoardPage == index) Color.White else Color(0xFF005FAF),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}
