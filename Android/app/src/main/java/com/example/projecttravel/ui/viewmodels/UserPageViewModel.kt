package com.example.projecttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.projecttravel.model.UserResponse
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.PlansDataRead
import com.example.projecttravel.model.SendSignIn
import com.example.projecttravel.model.UserInterest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserPageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UserPageUiState())
    val userPageUiState: StateFlow<UserPageUiState> = _uiState.asStateFlow()

    /** login ============================== */

    fun setUser (currentUser: UserResponse) {
        _uiState.update { currentState ->
            currentState.copy(currentLogin = currentUser)
        }
    }

    fun setCurrentSignIn (currentUser: SendSignIn) {
        _uiState.update { currentState ->
            currentState.copy(currentSignIn = currentUser)
        }
    }

    /** MyPage ============================== */
    fun setUserPageInfo (currentUser: UserResponse?) {
        _uiState.update { currentState ->
            currentState.copy( checkOtherUser = currentUser)
        }
    }

    fun setUserInterest (interest: UserInterest?) {
        _uiState.update { currentState ->
            currentState.copy( checkMyInterest = interest)
        }
    }

    fun setSimilarUsers (currentUser: List<UserResponse>) {
        _uiState.update { currentState ->
            currentState.copy( checkSimilarUsers = currentUser)
        }
    }

    fun setUserPlanList (currentPlanList: List<PlansDataRead>) {
        _uiState.update { currentState ->
            currentState.copy( checkMyPlanList = currentPlanList)
        }
    }

    fun setCheckUserPlanDataPage (currentPlan: PlansDataRead?) {
        _uiState.update { currentState ->
            currentState.copy( checkMyPageTrip = currentPlan)
        }
    }

    fun setUserPlanDate (currentPlanDate: String?) {
        _uiState.update { currentState ->
            currentState.copy( currentMyPlanDate = currentPlanDate)
        }
    }

    fun setUserPageTab (currentTab: Int) {
        _uiState.update { currentState ->
            currentState.copy( currentSelectedUserTab = currentTab)
        }
    }

    fun previousScreenWasPageOneA (click: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(previousScreenWasPageOneA = click)
        }
    }


    /** etc ============================== */
    fun setBackHandlerClick (backClick: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isBackHandlerClick = backClick)
        }
    }
    /** reset all Objects */
    fun resetUser() {
        _uiState.value = UserPageUiState() // UserUiState를 기본 값(null)으로 재설정
    }
}
