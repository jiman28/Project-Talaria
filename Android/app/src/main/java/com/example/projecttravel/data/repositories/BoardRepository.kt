package com.example.projecttravel.data.repositories

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
import com.example.projecttravel.network.TravelApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository retrieves Board data from underlying data source.
 */
interface BoardRepository {
    /** Retrieves list of Board from underlying data source */
    suspend fun getBoardList(callBoard: CallBoard): BoardList
    suspend fun getCompanyList(callBoard: CallBoard): CompanyList
    suspend fun getTradeList(callBoard: CallBoard): TradeList
    suspend fun getReplyList(callReply: CallReply): List<ReplyList>

    suspend fun setViewCounter(tabtitle: String, articleNo: String)

    suspend fun addArticle(sendArticle: SendArticle): Boolean
    suspend fun removeArticle(removeArticle: RemoveArticle): Boolean

    suspend fun addComment(sendComment: SendComment): Boolean
    suspend fun removeComment(removeComment: RemoveComment): Boolean

}

/** Network Implementations */
class DefaultBoardRepository(
    private val travelApiService: TravelApiService,
    private val travelStringApiService: TravelApiService,
) : BoardRepository {

    override suspend fun getBoardList(callBoard: CallBoard): BoardList {
        return withContext(Dispatchers.IO) {
            travelApiService.callBoardList(callBoard).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }

    override suspend fun getCompanyList(callBoard: CallBoard): CompanyList {
        return withContext(Dispatchers.IO) {
            travelApiService.callCompanyList(callBoard).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }

    override suspend fun getTradeList(callBoard: CallBoard): TradeList {
        return withContext(Dispatchers.IO) {
            travelApiService.callTradeList(callBoard).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }

    override suspend fun getReplyList(callReply: CallReply): List<ReplyList> {
        return withContext(Dispatchers.IO) {
            travelApiService.callReplyList(callReply).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }

    override suspend fun setViewCounter(tabtitle: String, articleNo: String) {
        return withContext(Dispatchers.IO) {
            travelStringApiService.setView(tabtitle = tabtitle, articleNo = articleNo).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }

    override suspend fun addArticle(sendArticle: SendArticle): Boolean {
        return withContext(Dispatchers.IO) {
            travelApiService.sendArticle(sendArticle).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }

    override suspend fun removeArticle(removeArticle: RemoveArticle): Boolean {
        return withContext(Dispatchers.IO) {
            travelApiService.removeArticle(removeArticle).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }

    override suspend fun addComment(sendComment: SendComment): Boolean {
        return withContext(Dispatchers.IO) {
            travelApiService.sendReply(sendComment).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }

    override suspend fun removeComment(removeComment: RemoveComment): Boolean {
        return withContext(Dispatchers.IO) {
            travelApiService.removeReply(removeComment).execute().body()
                ?: throw Exception("Unable to fetch board list")
        }
    }
}


