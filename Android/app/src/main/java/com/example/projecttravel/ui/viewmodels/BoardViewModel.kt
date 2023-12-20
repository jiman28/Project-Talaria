package com.example.projecttravel.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projecttravel.R
import com.example.projecttravel.TravelApplication
import com.example.projecttravel.data.repositories.BoardRepository
import com.example.projecttravel.model.AllBoardsEntity
import com.example.projecttravel.model.BoardList
import com.example.projecttravel.model.CallBoard
import com.example.projecttravel.model.CallReply
import com.example.projecttravel.model.CompanyList
import com.example.projecttravel.model.RemoveArticle
import com.example.projecttravel.model.RemoveComment
import com.example.projecttravel.model.ReplyList
import com.example.projecttravel.model.SendArticle
import com.example.projecttravel.model.SendComment
import com.example.projecttravel.model.TradeList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BoardListUiState {
    data class Success(val boardList: BoardList?) : BoardListUiState
    object Error : BoardListUiState
    object Loading : BoardListUiState
}

sealed interface CompanyListUiState {
    data class Success(val companyList: CompanyList?) : CompanyListUiState
    object Error : CompanyListUiState
    object Loading : CompanyListUiState
}

sealed interface TradeListUiState {
    data class Success(val tradeList: TradeList?) : TradeListUiState, ReplyListUiState
    object Error : TradeListUiState
    object Loading : TradeListUiState
}

sealed interface ReplyListUiState {
    data class Success(val replyList: List<ReplyList>) : ReplyListUiState
    object Error : ReplyListUiState
    object Loading : ReplyListUiState
}

sealed interface AddArticleUiState {
    data class Success(val addArticle: Boolean?) : AddArticleUiState
    object Error : AddArticleUiState
    object Loading : AddArticleUiState
}

sealed interface RemoveArticleUiState {
    data class Success(val removeArticle: Boolean?) : RemoveArticleUiState
    object Error : RemoveArticleUiState
    object Loading : RemoveArticleUiState
}

sealed interface AddCommentUiState {
    data class Success(val addComment: Boolean?) : AddCommentUiState
    object Error : AddCommentUiState
    object Loading : AddCommentUiState
}

sealed interface RemoveCommentUiState {
    data class Success(val removeComment: Boolean?) : RemoveCommentUiState
    object Error : RemoveCommentUiState
    object Loading : RemoveCommentUiState
}

data class BoardUiState(
    /** for Search ============================== */
    val currentSearchKeyWord: String = "",
    val currentSearchType: Int = R.string.selectSearchType,
    val currentSearchUser: String = "",

    val currentBoardPage: Int = 0,
    val currentCompanyPage: Int = 0,
    val currentTradePage: Int = 0,

    /** for View Article ============================== */
    val currentSelectedBoardTab: Int = R.string.boardTabTitle,
    val selectedViewBoard: AllBoardsEntity? = null,

    /** for ~~ ============================== */
    val selectedWriteBoardMenu: Int = R.string.selectTabTitle,
)

class BoardViewModel(private val boardRepository: BoardRepository) : ViewModel() {

    /** all connection states */
    var boardListUiState: BoardListUiState by mutableStateOf(BoardListUiState.Success(null))
        private set

    var companyListUiState: CompanyListUiState by mutableStateOf(CompanyListUiState.Success(null))
        private set

    var tradeListUiState: TradeListUiState by mutableStateOf(TradeListUiState.Success(null))
        private set

    var replyListUiState: ReplyListUiState by mutableStateOf(ReplyListUiState.Success(emptyList()))
        private set

    var addArticleUiState: AddArticleUiState by mutableStateOf(AddArticleUiState.Success(null))
        private set

    var removeArticleUiState: RemoveArticleUiState by mutableStateOf(
        RemoveArticleUiState.Success(
            null
        )
    )
        private set

    var addCommentUiState: AddCommentUiState by mutableStateOf(AddCommentUiState.Success(null))
        private set

    var removeCommentUiState: RemoveCommentUiState by mutableStateOf(
        RemoveCommentUiState.Success(
            null
        )
    )
        private set

    /** all selection states */
    private val _boardUiState = MutableStateFlow(BoardUiState())
    val boardUiState: StateFlow<BoardUiState> = _boardUiState.asStateFlow()

//    init {
//        getBoard(callBoard: CallBoard)
//    }

    /** connection functions */
    // 리뷰 게시판
    fun getBoardList(callBoard: CallBoard) {
        viewModelScope.launch {
            boardListUiState = BoardListUiState.Loading
            boardListUiState = try {
                BoardListUiState.Success(boardRepository.getBoardList(callBoard))
            } catch (e: IOException) {
                Log.d("jimanLog=getBoardList", "IOException: ${e.message}")
                BoardListUiState.Error
            } catch (e: HttpException) {
                Log.d("jimanLog=getBoardList", "HttpException: ${e.message}")
                BoardListUiState.Error
            }
        }
    }

    // 동행인 게시판
    fun getCompanyList(callBoard: CallBoard) {
        viewModelScope.launch {
            companyListUiState = CompanyListUiState.Loading
            companyListUiState = try {
                CompanyListUiState.Success(boardRepository.getCompanyList(callBoard))
            } catch (e: IOException) {
                Log.d("jimanLog=getCompanyList", "${e.message}")
                CompanyListUiState.Error
            } catch (e: HttpException) {
                Log.d("jimanLog=getCompanyList", "${e.message}")
                CompanyListUiState.Error
            }
        }
    }

    // 거래 게시판
    fun getTradeList(callBoard: CallBoard) {
        viewModelScope.launch {
            tradeListUiState = TradeListUiState.Loading
            tradeListUiState = try {
                TradeListUiState.Success(boardRepository.getTradeList(callBoard))
            } catch (e: IOException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                TradeListUiState.Error
            } catch (e: HttpException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                TradeListUiState.Error
            }
        }
    }
    
    // 댓글 목록
    fun getReplyList(callReply: CallReply) {
        viewModelScope.launch {
            replyListUiState = ReplyListUiState.Loading
            replyListUiState = try {
                ReplyListUiState.Success(boardRepository.getReplyList(callReply))
            } catch (e: IOException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                ReplyListUiState.Error
            } catch (e: HttpException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                ReplyListUiState.Error
            }
        }
    }

    // 게시글 조회수
    fun setViewCounter(tabtitle: String, articleNo: String) {
        viewModelScope.launch {
            boardRepository.setViewCounter(tabtitle = tabtitle, articleNo = articleNo)
        }
    }

    // 게시글 추가
    fun addArticle(sendArticle: SendArticle) {
        viewModelScope.launch {
            addArticleUiState = AddArticleUiState.Loading
            addArticleUiState = try {
                AddArticleUiState.Success(boardRepository.addArticle(sendArticle))
            } catch (e: IOException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                AddArticleUiState.Error
            } catch (e: HttpException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                AddArticleUiState.Error
            }
        }
    }
    // 무한 로딩 방지용 초기화
    fun resetAddArticle() {
        addArticleUiState = AddArticleUiState.Success(null)
    }

    // 게시글 삭제
    fun removeArticle(removeArticle: RemoveArticle) {
        viewModelScope.launch {
            removeArticleUiState = RemoveArticleUiState.Loading
            removeArticleUiState = try {
                RemoveArticleUiState.Success(boardRepository.removeArticle(removeArticle))
            } catch (e: IOException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                RemoveArticleUiState.Error
            } catch (e: HttpException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                RemoveArticleUiState.Error
            }
        }
    }
    // 무한 로딩 방지용 초기화
    fun resetRemoveArticle() {
        removeArticleUiState = RemoveArticleUiState.Success(null)
    }

    // 댓글 추가
    fun addComment(sendComment: SendComment) {
        viewModelScope.launch {
            addCommentUiState = AddCommentUiState.Loading
            addCommentUiState = try {
                AddCommentUiState.Success(boardRepository.addComment(sendComment))
            } catch (e: IOException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                AddCommentUiState.Error
            } catch (e: HttpException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                AddCommentUiState.Error
            }
        }
    }
    // 무한 로딩 방지용 초기화
    fun resetAddComment() {
        addCommentUiState = AddCommentUiState.Success(null)
    }

    // 댓글 삭제
    fun removeComment(removeComment: RemoveComment) {
        viewModelScope.launch {
            removeCommentUiState = RemoveCommentUiState.Loading
            removeCommentUiState = try {
                RemoveCommentUiState.Success(boardRepository.removeComment(removeComment))
            } catch (e: IOException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                RemoveCommentUiState.Error
            } catch (e: HttpException) {
                Log.d("jimanLog=getTradeList", "${e.message}")
                RemoveCommentUiState.Error
            }
        }
    }
    // 무한 로딩 방지용 초기화
    fun resetRemoveComment() {
        removeCommentUiState = RemoveCommentUiState.Success(null)
    }

    /** selection functions */
    /** for Search ============================== */
    fun setSearchKeyWord(kw: String) { _boardUiState.update { currentState -> currentState.copy(currentSearchKeyWord = kw) } }

    fun setSearchType(desiredBoard: Int) {_boardUiState.update { currentState -> currentState.copy(currentSearchType = desiredBoard) } }

    fun setSearchUser(email: String) { _boardUiState.update { currentState -> currentState.copy(currentSearchUser = email) } }

    fun setBoardPage(page: Int) { _boardUiState.update { currentState -> currentState.copy(currentBoardPage = page) } }

    fun setCompanyPage(page: Int) { _boardUiState.update { currentState -> currentState.copy(currentCompanyPage = page) } }

    fun setTradePage(page: Int) { _boardUiState.update { currentState -> currentState.copy(currentTradePage = page) } }

    /** for View Article ============================== */
    fun setBoardTab(tab: Int) { _boardUiState.update { currentState -> currentState.copy(currentSelectedBoardTab = tab) } }

    fun setViewBoard(viewBoard: AllBoardsEntity) { _boardUiState.update { currentState -> currentState.copy(selectedViewBoard = viewBoard) } }

    /** for ~~ ============================== */
    /** for ~~ ============================== */
    /** for ~~ ============================== */

    fun setWriteBoardMenu(desiredBoard: Int) { _boardUiState.update { currentState -> currentState.copy(selectedWriteBoardMenu = desiredBoard) } }

    /** etc ============================== */
    /** reset all Objects */
    fun resetBoardPage() { _boardUiState.value = BoardUiState() }


    /** viewModelFactory */
    companion object {
        val BoardFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as TravelApplication)
                val boardRepository = application.container.boardRepository
                BoardViewModel(boardRepository = boardRepository)
            }
        }
    }
}
