package com.example.projecttravel.network

import com.example.projecttravel.model.Board
import com.example.projecttravel.model.BoardList
import com.example.projecttravel.model.CallBoard
import com.example.projecttravel.model.CallReply
import com.example.projecttravel.model.CheckOtherUserById
import com.example.projecttravel.model.Company
import com.example.projecttravel.model.Reply
import com.example.projecttravel.model.Trade
import com.example.projecttravel.model.CityInfo
import com.example.projecttravel.model.CompanyList
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.model.GetAttrWeather
import com.example.projecttravel.model.InterestInfo
import com.example.projecttravel.model.TourAttractionSearchInfo
import com.example.projecttravel.model.TourAttractionInfo
import com.example.projecttravel.model.SpotDtoResponse
import com.example.projecttravel.model.User
import com.example.projecttravel.model.UserInterest
import com.example.projecttravel.model.UserResponse
import com.example.projecttravel.model.WeatherCallSend
import com.example.projecttravel.model.WeatherResponseGet
import com.example.projecttravel.model.RemoveArticle
import com.example.projecttravel.model.RemoveComment
import com.example.projecttravel.model.SendArticle
import com.example.projecttravel.model.SendComment
import com.example.projecttravel.model.PlansData
import com.example.projecttravel.model.PlansDataRead
import com.example.projecttravel.model.ReplyList
import com.example.projecttravel.model.SendInterest
import com.example.projecttravel.model.SendSignIn
import com.example.projecttravel.model.TradeList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface TravelApiService {
    // User DB Calls ==============================
    // Login Calls
    @POST("androidlogin")
    fun checkLogin(@Body user: User): Call<UserResponse>

    // SignIn Call
    @POST("androidsignin")
    @FormUrlEncoded
    fun androidSignIn(@Field("email") email: String?, @Field("name") name: String?, @Field("password") password: String?): Call<String>

    // Saving First Interest rate
    @POST("androidfirstinterest")
    fun saveFirstInterest(@Body sendInterest: SendInterest): Call<Boolean>

    // likeme
    @POST("likeme")
    fun getLikeMeUser(@Body userResponse: UserResponse): Call<List<UserResponse>>

    // userpage
    @POST("seepersonalpage")
    fun getOtherUserInfo(@Body checkOtherUserById: CheckOtherUserById): Call<UserResponse>

    // userpage
    @POST("callplanlist")
    fun getMyPlanList(@Body userResponse: UserResponse): Call<List<PlansDataRead>>

    // userpage
    @POST("callmyinterest")
    fun getMyInterest(@Body userResponse: UserResponse): Call<UserInterest>

    // Saving First Interest rate
    @POST("calledituser")
    fun sendEditUser(@Body sendSignIn: SendSignIn): Call<Boolean>

    // Saving First Interest rate
    @POST("callwithdrawaluser")
    fun sendWithdrawUser(@Body sendSignIn: SendSignIn): Call<Boolean>


    // UserInfo DB ==============================
    @GET("userinfo")
    suspend fun getUserInfoList(): List<UserInterest>

    @GET("planlist")
    suspend fun getUserPlansDataList(): List<PlansDataRead>


    // Travel DB ==============================
    @GET("country")
    suspend fun getCountryList(): List<CountryInfo>

    @GET("city")
    suspend fun getCityList(): List<CityInfo>

    @GET("interest")
    suspend fun getInterestList(): List<InterestInfo>

    @GET("tourattraction")
    suspend fun getTourAttractionList(): List<TourAttractionInfo>

    @GET("search")
    suspend fun getTourAttrSearchList(): List<TourAttractionSearchInfo>


    // Travel DB Edit Calls ==============================
    // send placeName to searchedPlaceInfo
    @POST("sendplacename")
    @FormUrlEncoded
    fun setPlaceName(@Field("placeName") placeName: String?, @Field("cityId") cityId: String?, @Field("stateInOut") stateInOut: String,): Call<String>

    // send inout Info to DB
    @POST("sendinout")
    @FormUrlEncoded
    fun setInOut(@Field("placeName") placeName: String, @Field("stateInOut") stateInOut: String, ): Call<String>

    // send date to get WeatherInfos
    @POST("sendDate")
    fun getDateWeather(@Body weatherCallSend: WeatherCallSend): Call<List<WeatherResponseGet>>

    // send date to get AttrList by Weather
    @POST("sendAttrWeather")
    fun getDateWeatherAttr(@Body getAttrWeather: GetAttrWeather): Call<List<SpotDtoResponse>>

    // send date to get AttrList by City ~ OnWorking
    @POST("sendAttrCity")
    fun getDateCityAttr(@Body getAttrWeather: GetAttrWeather): Call<List<SpotDtoResponse>>

    // save plan
    @POST("saveplan")
    fun addPlan(@Body plansData: PlansData): Call<Boolean>

    // delete plan
    @POST("deleteplan")
    @FormUrlEncoded
    fun deletePlan(@Field("planId") planId: String,): Call<String>

    /** ======================================================================================== */
    /** refactoring - MVVM ===================================================================== */

    /** Board DB Calls - MVVM ===================================================================== */
    @POST("callboardlist")
    fun callBoardList(@Body callBoard: CallBoard): Call<BoardList>

    @POST("callcompanylist")
    fun callCompanyList(@Body callBoard: CallBoard): Call<CompanyList>

    @POST("calltradelist")
    fun callTradeList(@Body callBoard: CallBoard): Call<TradeList>

    @POST("callreplylist")
    fun callReplyList(@Body callReply: CallReply): Call<List<ReplyList>>
    // Board DB Edit Calls ==============================

    // viewCounter
    @POST("sendviewcount")
    @FormUrlEncoded
    fun setView(@Field("tabtitle") tabtitle: String, @Field("articleNo") articleNo: String, ): Call<String>

    // write comment,reply
    @POST("sendreply")
    fun sendReply(@Body sendComment: SendComment): Call<Boolean>

    // delete comment,reply
    @POST("removereply")
    fun removeReply(@Body removeComment: RemoveComment): Call<Boolean>

    // write Article
    @POST("sendarticle")
    fun sendArticle(@Body sendArticle: SendArticle): Call<Boolean>

    // delete Article
    @POST("removearticle")
    fun removeArticle(@Body removeArticle: RemoveArticle): Call<Boolean>
}
