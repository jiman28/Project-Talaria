package com.example.projecttravel.data.uistates

import com.example.projecttravel.R
import com.example.projecttravel.model.PlansDataRead
import com.example.projecttravel.model.SendSignIn
import com.example.projecttravel.model.UserInterest
import com.example.projecttravel.model.UserResponse

data class UserPageUiState(
    /** login ============================== */
    val currentLogin: UserResponse? = null,
    val currentSignIn: SendSignIn? =null,

    /** MyPage ============================== */
    val checkOtherUser: UserResponse? = null,
    val checkMyInterest: UserInterest? = null,
    val checkSimilarUsers: List<UserResponse> = emptyList(),
    val checkMyPlanList: List<PlansDataRead> = emptyList(),
    val checkMyPageTrip: PlansDataRead? = null,
    val currentMyPlanDate: String? = null,
    val currentSelectedUserTab: Int = R.string.userTabMenuBoard,
    val previousScreenWasPageOneA: Boolean = false,

    /** etc ============================== */
    val isBackHandlerClick: Boolean = false,
)
