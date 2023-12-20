package com.example.projecttravel.ui.screens.boardview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttravel.BuildConfig
import com.example.projecttravel.R
import com.example.projecttravel.ui.viewmodels.BoardUiState
import com.example.projecttravel.ui.viewmodels.BoardViewModel
import com.example.projecttravel.ui.viewmodels.RemoveArticleUiState
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.model.BoardEntity
import com.example.projecttravel.model.CallBoard
import com.example.projecttravel.model.CompanyEntity
import com.example.projecttravel.model.RemoveArticle
import com.example.projecttravel.model.TradeEntity
import com.example.projecttravel.model.UserDto
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import org.jsoup.Jsoup

@Composable
fun ViewContentsBoard(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    boardViewModel: BoardViewModel,
    boardUiState: BoardUiState,
    onBackButtonClicked: () -> Unit,
    onUserButtonClicked: () -> Unit,
) {

    val currentArticleNo: Int = when (boardUiState.selectedViewBoard) {
        is BoardEntity -> boardUiState.selectedViewBoard.articleNo
        is CompanyEntity -> boardUiState.selectedViewBoard.articleNo
        is TradeEntity -> boardUiState.selectedViewBoard.articleNo
        else -> 0
    }
    val currentTitle: String = when (boardUiState.selectedViewBoard) {
        is BoardEntity -> boardUiState.selectedViewBoard.title
        is CompanyEntity -> boardUiState.selectedViewBoard.title
        is TradeEntity -> boardUiState.selectedViewBoard.title
        else -> ""
    }
    val currentContent: String = when (boardUiState.selectedViewBoard) {
        is BoardEntity -> boardUiState.selectedViewBoard.content
        is CompanyEntity -> boardUiState.selectedViewBoard.content
        is TradeEntity -> boardUiState.selectedViewBoard.content
        else -> ""
    }
    val currentWriteDate: String = when (boardUiState.selectedViewBoard) {
        is BoardEntity -> boardUiState.selectedViewBoard.writeDate
        is CompanyEntity -> boardUiState.selectedViewBoard.writeDate
        is TradeEntity -> boardUiState.selectedViewBoard.writeDate
        else -> ""
    }
    val currentViews: Int = when (boardUiState.selectedViewBoard) {
        is BoardEntity -> boardUiState.selectedViewBoard.views
        is CompanyEntity -> boardUiState.selectedViewBoard.views
        is TradeEntity -> boardUiState.selectedViewBoard.views
        else -> 0
    }
    val currentWriteId: String = when (boardUiState.selectedViewBoard) {
        is BoardEntity -> boardUiState.selectedViewBoard.writeId
        is CompanyEntity -> boardUiState.selectedViewBoard.writeId
        is TradeEntity -> boardUiState.selectedViewBoard.writeId
        else -> ""
    }
    val currentUser: UserDto? = when (boardUiState.selectedViewBoard) {
        is BoardEntity -> boardUiState.selectedViewBoard.user
        is CompanyEntity -> boardUiState.selectedViewBoard.user
        is TradeEntity -> boardUiState.selectedViewBoard.user
        else -> null
    }

    val tabtitle = stringResource(boardUiState.currentSelectedBoardTab)

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

    when (boardViewModel.removeArticleUiState) {
        is RemoveArticleUiState.Loading -> isLoadingState = true
        is RemoveArticleUiState.Success -> {
            if ((boardViewModel.removeArticleUiState as RemoveArticleUiState.Success).removeArticle == true) {
                isLoadingState = null
                boardViewModel.getBoardList(callBoard)
                boardViewModel.getCompanyList(callCompany)
                boardViewModel.getTradeList(callTrade)
                boardViewModel.resetRemoveArticle()
                onBackButtonClicked()
            } else if ((boardViewModel.removeArticleUiState as RemoveArticleUiState.Success).removeArticle == false) {
                isLoadingState = false
                boardViewModel.resetRemoveArticle()
                txtErrorMsg = "게시글 삭제 실패"
            }
        }
        else -> {
            isLoadingState = false
            txtErrorMsg = "게시글 삭제 실패"
        }
    }

    /** UI 관리 ============================== */
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(start = 15.dp, end = 15.dp, top = 15.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
        ) {
            Text(
                fontSize = 40.sp,
                text = when (boardUiState.currentSelectedBoardTab) {
                    R.string.boardTabTitle -> stringResource(R.string.boardTabTitle)
                    R.string.tradeTabTitle -> stringResource(R.string.tradeTabTitle)
                    R.string.companyTabTitle -> stringResource(R.string.companyTabTitle)
                    else -> ""
                }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                fontSize = 25.sp,
                text = "게시판입니다"
            )
        }

        Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

        Column(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
        ) {
            Column {
                Text(fontSize = 25.sp, text = currentTitle)
            }

            Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(fontSize = 12.sp, text = currentArticleNo.toString())
                Spacer(modifier = Modifier.padding(5.dp))

                if (currentUser != null) {
                    if (currentUser.picture != null) {
                        AsyncImage(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(50.dp)),
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(currentUser.picture)
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
                }
                Spacer(modifier = Modifier.padding(1.dp))
                UserDropDownMenu(
                    boardUiState = boardUiState,
                    boardViewModel = boardViewModel,
                    userPageViewModel = userPageViewModel,
                    onUserButtonClicked = onUserButtonClicked,
                )
                Spacer(modifier = Modifier.padding(5.dp))

                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = "WriteDate"
                )
                Spacer(modifier = Modifier.padding(1.dp))
                Text(fontSize = 12.sp, text = currentWriteDate.substring(0 until 10))

                Spacer(modifier = Modifier.padding(5.dp))

                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.Filled.RemoveRedEye,
                    contentDescription = "Views"
                )
                Spacer(modifier = Modifier.padding(1.dp))
                Text(fontSize = 12.sp, text = currentViews.toString())
            }

            Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

            Column {
                // HTML 문자열을 파싱하여 DOM 생성
                val doc = Jsoup.parse(currentContent)
                // 이미지 태그를 찾아 스타일을 추가하거나 크기를 조절
                val imgTags = doc.select("img")
                for (imgTag in imgTags) {
                    imgTag.attr("style", "max-width:100%; height:auto;") // 이미지 크기 조절 스타일 추가
                    // 다른 스타일을 추가하거나 수정할 수 있습니다.
                }
                // 수정된 HTML 문자열 생성
                val modifiedContent = doc.toString()
                // Compose의 WebView에 표시
                HTMLContentWithImage(modifiedContent)
            }
            Spacer(modifier = Modifier.padding(5.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.End, // 수평 가운데 정렬
            ) {
                var isRemoveArticleDialog by remember { mutableStateOf(false) }
                if (userPageUiState.currentLogin?.id == currentUser?.id.toString()) {
                    Button(
                        onClick = { isRemoveArticleDialog = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                        ) {
                            Icon(
                                modifier = Modifier.size(15.dp),
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = "CancelComment"
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(text = "게시글 삭제")
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                }
                if (isRemoveArticleDialog) {
                    val removeArticle = RemoveArticle(
                        tabTitle = tabtitle,
                        articleNo = currentArticleNo.toString(),
                    )
                    RemoveArticleDialog(
                        removeArticle = removeArticle,
                        boardViewModel = boardViewModel,
                        onDismiss = {
                            isRemoveArticleDialog = false
                        },
                    )
                }
            }
            Divider(thickness = dimensionResource(R.dimen.thickness_divider3))
            Spacer(modifier = Modifier.padding(5.dp))
            Column {
                ShowReply(
                    replyListUiState = boardViewModel.replyListUiState,
                    boardUiState = boardUiState,
                    boardViewModel = boardViewModel,
                    userPageUiState = userPageUiState,
                    currentArticleNo = currentArticleNo,
                )
            }
        }
    }
}

/** localhost IPv4 BuildConfig for WebView */
private val BASE_BOARD_SUMMERNOTE = BuildConfig.BASE_BOARD_SUMMERNOTE

/** html parser for Text and Img (summernote) ====================*/
@Composable
fun HTMLContentWithImage(content: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // WebView 설정
                settings.javaScriptEnabled = true // JavaScript 활성화 여부 설정
                webViewClient = WebViewClient()
            }
        },
        update = { webView ->
            // WebView에 이미지 URL을 로드하고 HTML을 표시
            webView.loadDataWithBaseURL(BASE_BOARD_SUMMERNOTE, content, "text/html", "utf-8", null)
        }
    )
}

@Composable
fun RemoveArticleDialog(
    removeArticle: RemoveArticle,
    boardViewModel: BoardViewModel,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "게시글을 삭제하시겠습니까?",
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
                        boardViewModel.removeArticle(removeArticle)
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
